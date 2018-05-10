package hft.wiinf.de.horario.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

//class for a person with can (not) participate at an appointment or be the creator of an appointment
@Table(name="persons")
public class Person extends Model{
    @Column
    String phoneNumber = "";
    @Column
    String name="";
    @Column(name = "event_Accepted")
    private Event acceptedEvent = null;
    @Column(name = "event_canceled")
    private Event canceledEvent = null;
    @Column
    boolean isItMe = false;
    @Column
    boolean enablePush = false;
    @Column
    int pushMinutes=0;

    //Use this constructor for person that using this specific app (owner)
    public Person(boolean isItMe, String phoneNumber, String name) {
        super();
        this.isItMe = isItMe;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    //Use this constructor for persons that is not the current(real) user of this specific app
    public Person(String phoneNumber, String name) {
        super();
        this.isItMe = false;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    public Person(String phoneNumber, int pushMinutes) {
        super();
        this.phoneNumber = phoneNumber;
        this.pushMinutes = pushMinutes;
    }

    public Person() {
        super();
    }

    public boolean isItMe() {
        return isItMe;
    }

    public void setItMe(boolean itMe) {
        this.isItMe = itMe;
    }

    //getter-setter
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCanceledEvent(Event canceledEvent) {
        this.canceledEvent = canceledEvent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAcceptedEvent(Event acceptedEvent) {
        this.acceptedEvent = acceptedEvent;
    }

    public int getPushMinutes() {
        return pushMinutes;
    }

    public void setPushMinutes(int pushMinutes) {
        this.pushMinutes = pushMinutes;
    }

    public boolean isEnablePush() {
        return enablePush;
    }

    public void setEnablePush(boolean enablePush) {
        this.enablePush = enablePush;
    }
}

