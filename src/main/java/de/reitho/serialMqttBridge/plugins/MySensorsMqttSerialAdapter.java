package de.reitho.serialMqttBridge.plugins;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MySensorsMqttSerialAdapter implements MqttPublishPreprocessingPlugin, SerialSendPreprocessingPlugin {

  private String mqttTopicPath;
  private String mqttPublishMessage;
  private String serialMessage;

  /* ********************************************************************************************************************************************************************
   * (non-Javadoc)
   * @see de.reitho.serialMqttBridge.plugins.MqttPreprocessingPlugin#getPluginName()
   */
  @Override
  public String getPluginName() {
    return "MySensors MQTT<->Serial adapter plugin";
  }

  /* ********************************************************************************************************************************************************************
   * (non-Javadoc)
   * @see de.reitho.serialMqttBridge.plugins.AbstractPreprocessorPlugin#getPluginDescription()
   */
  @Override
  public String getPluginDescription() {
    return "This plugin converts converts MySensors serial message format into topic and message for MQTT publishing and vice-versa.";
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

  /* ********************************************************************************************************************************************************************
   * (non-Javadoc)
   * @see de.reitho.serialMqttBridge.plugins.SerialSendPreprocessingPlugin#processMessage(java.lang.String, org.eclipse.paho.client.mqttv3.MqttMessage)
   */
  @Override
  public boolean processMqttMessage(String mqttTopic, String mqttTopicSubscribe, MqttMessage mqttMessage) throws Exception {

    serialMessage = "";

    /*
     *  Get rid of subscribed topic prefix
     */

    // Handle possible topic wildcards
    String mqttTopicPrefix = mqttTopicSubscribe.replaceAll("/#", "");
    mqttTopicPrefix = mqttTopicPrefix.replaceAll("/+", "");

    mqttTopic = mqttTopic.substring(mqttTopicPrefix.length());
    if (mqttTopic.startsWith("/")) {
      mqttTopic = mqttTopic.substring(1);
    }
    mqttTopic = mqttTopic.replaceAll("/", ";");

    serialMessage = mqttTopic;
    // Append payload if there is any
    if (mqttMessage.getPayload().length > 0) {
      serialMessage += ";" + new String(mqttMessage.getPayload());
    }

    return true;
  }

  /* ********************************************************************************************************************************************************************
   * (non-Javadoc)
   * @see de.reitho.serialMqttBridge.plugins.SerialSendPreprocessingPlugin#getSerialMessage()
   */
  @Override
  public String getSerialMessage() {
    return serialMessage;
  }
}
