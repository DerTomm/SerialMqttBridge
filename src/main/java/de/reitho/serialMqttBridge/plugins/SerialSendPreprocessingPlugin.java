package de.reitho.serialMqttBridge.plugins;

public interface SerialSendPreprocessingPlugin {

  public String getPluginName();

  public String getPluginDescription();

  public boolean processMessage(String mqttTopic, String mqttMessage) throws Exception;

  public String getSerialMessage();

}
