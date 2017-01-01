package de.reitho.serialMqttBridge.plugins;

public interface MqttPublishPreprocessingPlugin {

  public String getPluginName();

  public String getPluginDescription();

  public boolean processSerialMessage(String message) throws Exception;

  public String getPublishTopic();

  public String getPublishMessage();

}
