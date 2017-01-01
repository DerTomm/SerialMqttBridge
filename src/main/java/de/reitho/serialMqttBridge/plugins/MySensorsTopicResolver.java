package de.reitho.serialMqttBridge.plugins;

public class MySensorsTopicResolver implements MqttPublishPreprocessingPlugin {

  private String mqttTopicPath;
  private String mqttPublishMessage;

  /* ********************************************************************************************************************************************************************
   * (non-Javadoc)
   * @see de.reitho.serialMqttBridge.plugins.MqttPreprocessingPlugin#getPluginName()
   */
  @Override
  public String getPluginName() {
    return "MySensors MQTT Preprocessor Plugin";
  }

  /* ********************************************************************************************************************************************************************
   * (non-Javadoc)
   * @see de.reitho.serialMqttBridge.plugins.AbstractPreprocessorPlugin#getPluginDescription()
   */
  @Override
  public String getPluginDescription() {
    return "This plugin converts MySensors message format into topic and message for MQTT publishing by building the topic path using the ';' divider char.";
  }

  /* ********************************************************************************************************************************************************************
   * (non-Javadoc)
   * @see de.reitho.serialMqttBridge.plugins.AbstractPreprocessorPlugin#processMessage(java.lang.String)
   */
  @Override
  public boolean processMessage(String message) throws Exception {

    String[] topicTokens = message.split(";");
    for (int i = 0; i < topicTokens.length - 1; i++) {
      if (i > 0) {
        mqttTopicPath += "/";
      }
      mqttTopicPath += topicTokens[i];
    }

    mqttPublishMessage = topicTokens[topicTokens.length - 1];

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
