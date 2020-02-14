package org.endeavourhealth.common.security.ddsmodel.models.DAL;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.OrganisationEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.json.JsonOrganisationCCG;
import org.endeavourhealth.common.security.ddsmodel.models.json.JsonDDSOrganisationStatus;
import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;
import org.endeavourhealth.common.security.usermanagermodel.models.caching.OrganisationCache;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SecurityDDSDAL {

    public List<JsonDDSOrganisationStatus> getOrganisationStatus(List<String> odsCodes, String agreementName) throws Exception {

        EntityManager entityManager = ConnectionManager.getDdsEntityManager();

        try {
            Query query = entityManager.createQuery(
                    "select " +
                            "s.localId," +
                            "max(x.timestamp)," +
                            "case " +
                            "   when err.exchangeIdsInError is null then 0 " +
                            "   else 1 " +
                            "end" +
                            " from ServiceEntity s " +
                            "inner join ExchangeEntity x on x.serviceId = s.id " +
                            "left outer join ExchangeTransformErrorStateEntity err on err.serviceId = s.id " +
                            "where s.localId IN (:odsList) " +
                            "group by s.localId " +
                            "order by x.timestamp desc");
            query.setParameter("odsList", odsCodes);

            List<Object[]> result = query.getResultList();

            return processOrganisationStatus(result, odsCodes, agreementName);
        } finally {
            entityManager.close();
        }
    }

    private List<JsonDDSOrganisationStatus> processOrganisationStatus(List<Object[]> resultList, List<String> odsCodes, String agreementName) throws Exception {
        List<JsonDDSOrganisationStatus> orgStatusList = new ArrayList<>();

        for (Object[] row : resultList) {
            JsonDDSOrganisationStatus orgStatus = new JsonDDSOrganisationStatus();
            orgStatus.setOdsCode((String)row[0]);

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            orgStatus.setLastReceived(df.format((Date)row[1]));
            orgStatus.setInError((Integer)row[2] == 1 ? true : false);
            orgStatus.setReferenceAgreement(agreementName);
            orgStatusList.add(orgStatus);
        }

        List<String> missingOrgs = new ArrayList<>();

        Set<String> foundOdsSet =
                orgStatusList.stream()
                        .map(JsonDDSOrganisationStatus::getOdsCode)
                        .collect(Collectors.toSet());

        missingOrgs = odsCodes.stream().filter(org -> !foundOdsSet.contains(org)).collect(Collectors.toList());

        if (!missingOrgs.isEmpty()) {
            for (String ods: missingOrgs) {
                JsonDDSOrganisationStatus orgStatus = new JsonDDSOrganisationStatus();
                orgStatus.setOdsCode(ods);
                orgStatus.setInError(false);
                orgStatus.setReferenceAgreement(agreementName);
                orgStatusList.add(orgStatus);
            }
        }

        fillOrganisationDetails(orgStatusList, odsCodes);
        getCCG(orgStatusList, odsCodes);
        return orgStatusList;
    }

    private void fillOrganisationDetails(List<JsonDDSOrganisationStatus> orgs, List<String> odsCodes) throws Exception {
        List<OrganisationEntity> organisationEntities = OrganisationCache.getOrganisationDetailsFromOdsCodeList(odsCodes);

        if (organisationEntities.isEmpty()) {
            return;
        }

        for (JsonDDSOrganisationStatus org : orgs) {
            OrganisationEntity organisationEntity
                    = organisationEntities.stream().filter(orgEnt -> orgEnt.getOdsCode().equals(org.getOdsCode())).findFirst().orElse(null);

            if (organisationEntity != null) {
                org.setPracticeName(organisationEntity.getName());
                org.setSystemSupplierType(organisationEntity.getSystemSupplierSystemId());
                org.setSystemSupplierReference(organisationEntity.getSystemSupplierReference());
                org.setSharingActivated(organisationEntity.getSystemSupplierSharingActivated());
            }
        }
    }

    private void getCCG(List<JsonDDSOrganisationStatus> orgs, List<String> odsCodes) throws Exception {

        List<JsonOrganisationCCG> organisationCCGs = OrganisationCache.getCCGForOrganisationList(odsCodes);

        if (organisationCCGs.isEmpty()) {
            return;
        }

        for (JsonDDSOrganisationStatus org : orgs) {
            JsonOrganisationCCG orgCCG
                    = organisationCCGs.stream().filter(orgEnt -> orgEnt.getOdsCode().equals(org.getOdsCode())).findFirst().orElse(null);

            if (orgCCG != null) {
                org.setCcg(orgCCG.getCcgName());
            }
        }

    }
}
