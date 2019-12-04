package org.endeavourhealth.common.security.datasharingmanagermodel.models.DAL;

import org.endeavourhealth.common.security.datasharingmanagermodel.models.database.ProjectScheduleEntity;
import org.endeavourhealth.common.security.datasharingmanagermodel.models.json.JsonProjectSchedule;
import org.endeavourhealth.common.security.usermanagermodel.models.ConnectionManager;

import javax.persistence.EntityManager;

public class SecurityProjectScheduleDAL {

    public ProjectScheduleEntity get(String uuid) throws Exception {

        EntityManager entityManager = ConnectionManager.getDsmEntityManager();
        try {
            ProjectScheduleEntity entity = entityManager.find(ProjectScheduleEntity.class, uuid);
            return entity;
        } finally {
            entityManager.close();
        }
    }

    public void save(JsonProjectSchedule schedule) throws Exception {

        EntityManager entityManager = ConnectionManager.getDsmEntityManager();
        try {
            ProjectScheduleEntity entity = new ProjectScheduleEntity();
            entity.setUuid(schedule.getUuid());
            setValues(entity, schedule);
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public void update(JsonProjectSchedule schedule) throws Exception {

        EntityManager entityManager = ConnectionManager.getDsmEntityManager();
        try {
            ProjectScheduleEntity entity = entityManager.find(ProjectScheduleEntity.class, schedule.getUuid());
            if (entity != null) {
                entityManager.getTransaction().begin();
                setValues(entity, schedule);
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }

    private void setValues(ProjectScheduleEntity entity, JsonProjectSchedule schedule) {
        entity.setUuid(schedule.getUuid());
        entity.setCronExpression(schedule.getCronExpression());
        entity.setCronDescription(schedule.getCronDescription());
        entity.setCronSettings(schedule.getCronSettings());
    }

    public void delete(String uuid) throws Exception {

        EntityManager entityManager = ConnectionManager.getDsmEntityManager();
        try {
            ProjectScheduleEntity entity = entityManager.find(ProjectScheduleEntity.class, uuid);
            if (entity != null) {
                entityManager.getTransaction().begin();
                entityManager.remove(entity);
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
