package de.reitho.serialMqttBridge.plugins;

public class MySensorsCustomSerialMqttTimestampAdapter implements MqttPublishPreprocessingPlugin {

  private String mqttTopicPath;
  private String mqttPublishMessage;

  /* ********************************************************************************************************************************************************************
   * (non-Javadoc)
   * @see de.reitho.serialMqttBridge.plugins.MqttPreprocessingPlugin#getPluginName()
   */
  @Override
  public String getPluginName() {
    return "MySensors Serial to MQTT adapter plugin with timestamp addition";
  }

  /* ********************************************************************************************************************************************************************
   * (non-Javadoc)
   * @see de.reitho.serialMqttBridge.plugins.AbstractPreprocessorPlugin#getPluginDescription()
   */
  @Override
  public String getPluginDescription() {
    return "This plugin converts converts MySensors serial message format into topic and message for MQTT publishing (adding timestamp to payload).";
  }

  /* ********************************************************************************************************************************************************************
   * (non-Javadoc)
   * @see de.reitho.serialMqttBridge.plugins.AbstractPreprocessorPlugin#processMessage(java.lang.String)
   */
  @Override
  public boolean processSerialMessage(String message) throws Exception {

    if (message.length() == 0 || !message.contains(";")) {
      return false;
    }

    mqttTopicPath = "";
    mqttPublishMessage = "";

    String[] topicTokens = message.split(";");
    for (int i = 0; i < topicTokens.length - 1; i++) {
      if (i > 0) {
        mqttTopicPath += "/";
      }
      mqttTopicPath += topicTokens[i];
    }

    mqttPublishMessage = topicTokens[topicTokens.length - 1];

    // If message is of type V_TRIPPED (16): add timestamp to payload
    if (mqttTopicPath.endsWith("/16")) {
      mqttPublishMessage += ";" + (int) System.currentTimeMillis() / 1000;
    }

    return true;
  }

  /* ********************************************************************************************************************************************************************
   * (non-Javadoc)
   * @see de.reitho.serialMqttBridge.plugins.AbstractMqttPreprocessorPlugin#getPublishTopic()
   */
  @Override
  public String getPublishTopic() {
    return mqttTopicPath;
  }

  /* ********************************************************************************************************************************************************************
   * (non-Javadoc)
   * @see de.reitho.serialMqttBridge.plugins.AbstractMqttPreprocessorPlugin#getPublishMessage()
   */
  @Override
  public String getPublishMessage() {
    return mqttPublishMessage;
  }
}
