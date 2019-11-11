package org.endeavourhealth.common.security.datasharingmanagermodel.models.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "project_schedule", schema = "data_sharing_manager")
public class ProjectScheduleEntity {

    private String uuid;
    private Date starts;
    private Date ends;
    private int frequency;
    private String weeks;
    private byte isMonday;
    private byte isTuesday;
    private byte isWednesday;
    private byte isThursday;
    private byte isFriday;
    private byte isSaturday;
    private byte isSunday;

    @Id
    @Column(name = "uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "starts")
    public Date getStarts() {
        return starts;
    }

    public void setStarts(Date starts) {
        this.starts = starts;
    }

    @Column(name = "ends")
    public Date getEnds() {
        return ends;
    }

    public void setEnds(Date ends) {
        this.ends = ends;
    }

    @Column(name = "frequency")
    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Column(name = "weeks")
    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    @Column(name = "is_monday")
    public byte getIsMonday() {
        return isMonday;
    }

    public void setIsMonday(byte isMonday) {
        this.isMonday = isMonday;
    }

    @Column(name = "is_tuesday")
    public byte getIsTuesday() {
        return isTuesday;
    }

    public void setIsTuesday(byte isTuesday) {
        this.isTuesday = isTuesday;
    }

    @Column(name = "is_wednesday")
    public byte getIsWednesday() {
        return isWednesday;
    }

    public void setIsWednesday(byte isWednesday) {
        this.isWednesday = isWednesday;
    }

    @Column(name = "is_thursday")
    public byte getIsThursday() {
        return isThursday;
    }

    public void setIsThursday(byte isThursday) {
        this.isThursday = isThursday;
    }

    @Column(name = "is_friday")
    public byte getIsFriday() {
        return isFriday;
    }

    public void setIsFriday(byte isFriday) {
        this.isFriday = isFriday;
    }

    @Column(name = "is_saturday")
    public byte getIsSaturday() {
        return isSaturday;
    }

    public void setIsSaturday(byte isSaturday) {
        this.isSaturday = isSaturday;
    }

    @Column(name = "is_sunday")
    public byte getIsSunday() {
        return isSunday;
    }

    public void setIsSunday(byte isSunday) {
        this.isSunday = isSunday;
    }
}
