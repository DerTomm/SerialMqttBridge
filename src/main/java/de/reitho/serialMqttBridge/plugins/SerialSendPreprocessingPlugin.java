package de.reitho.serialMqttBridge.plugins;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface SerialSendPreprocessingPlugin {

  public String getPluginName();

  public String getPluginDescription();

  public boolean processMqttMessage(String mqttTopic, String mqttTopicSubscribe, MqttMessage mqttMessage) throws Exception;

  public String getSerialMessage();

}
