package com.msc.spring.consumer.message;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String requestType;
    private String correlationId;
    private Date receiveTime;
    private int messageVolume;
    private int messageSize;

    private boolean isMultiThreaded;

    public Message() {
    }

    public int getMessageVolume() {
        return messageVolume;
    }

    public void setMessageVolume(int messageVolume) {
        this.messageVolume = messageVolume;
    }

    public int getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(int messageSize) {
        this.messageSize = messageSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public boolean isMultiThreaded() {
        return isMultiThreaded;
    }

    public void setMultiThreaded(boolean multiThreaded) {
        isMultiThreaded = multiThreaded;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", requestType='" + requestType + '\'' +
                ", correlationId='" + correlationId + '\'' +
                ", receiveTime=" + receiveTime +
                ", messageVolume=" + messageVolume +
                ", messageSize=" + messageSize +
                ", isMultiThreaded=" + isMultiThreaded +
                '}';
    }
}
