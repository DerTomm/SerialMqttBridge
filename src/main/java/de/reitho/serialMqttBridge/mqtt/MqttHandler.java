package de.reitho.serialMqttBridge.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.reitho.serialMqttBridge.SerialMqttBridge;
import de.reitho.serialMqttBridge.config.ConfigHandler;
import de.reitho.serialMqttBridge.plugins.SerialSendPreprocessingPlugin;

public class MqttHandler {

  private Logger logger = LoggerFactory.getLogger(MqttHandler.class);

  private static MqttHandler instance;
  private SerialMqttBridge serialMqttBridge;

  private MqttClient mqttClient;
  private MqttSubscriptionCallback mqttCallback;

  /*********************************************************************************************************************************************************************
   * @param configHandler
   */
  private MqttHandler(SerialMqttBridge serialMqttBride) throws Exception {
    this.serialMqttBridge = serialMqttBride;
    connectAndSubscribe();
  }

  /*********************************************************************************************************************************************************************
   * @param configHandler
   * @return
   */
  public static MqttHandler getInstance(SerialMqttBridge serialMqttBride) throws Exception {
    if (instance == null) {
      instance = new MqttHandler(serialMqttBride);
    }
    return instance;
  }

  /*********************************************************************************************************************************************************************
   *
   */
  private void connectAndSubscribe() throws Exception {

    ConfigHandler configHandler = serialMqttBridge.getConfigHandler();

    mqttClient = new MqttClient(configHandler.getMqttBrokerUrl(), configHandler.getMqttClientId(), null);
    MqttConnectOptions connOpts = new MqttConnectOptions();
    connOpts.setCleanSession(true);

    // Authentication
    if (configHandler.getMqttBrokerUsername() != null && configHandler.getMqttBrokerPassword() != null) {
      connOpts.setUserName(configHandler.getMqttBrokerUsername());
      connOpts.setPassword(configHandler.getMqttBrokerPassword().toCharArray());
    }

    // MqttCallback
    mqttCallback = new MqttSubscriptionCallback(this);
    mqttClient.setCallback(mqttCallback);

    mqttClient.connect(connOpts);

    // Subscribe to defined inbound topic
    mqttClient.subscribe(configHandler.getMqttTopicSubscribe());
  }

  /*********************************************************************************************************************************************************************
   * @param topicPath
   * @param message
   */
  public void publishMessage(String messagePath, String message) {

    MqttMessage mqttMessage = new MqttMessage(message.getBytes());
    mqttMessage.setQos(2);

    String configuredTopicPublish = serialMqttBridge.getConfigHandler().getMqttTopicPublish();
    String mqttPublishTopic = configuredTopicPublish + "/" + messagePath;

    /*
     * If defined: Log outgoing MQTT message
     */
    if (serialMqttBridge.getConfigHandler().logMqttOutbound()) {
      logger.info("MQTT/Out: " + mqttPublishTopic + " | " + message);
    }

    /*
     * Publish MQTT message to broker
     */

    try {
      mqttClient.publish(mqttPublishTopic, mqttMessage);
    }
    catch (Exception e) {
      logger.error("Exception", e);
    }
  }

  /*********************************************************************************************************************************************************************
   *
   */
  public void processMessage(String topic, MqttMessage message) {

    if (!serialMqttBridge.isInitialized()) {
      return;
    }

    /*
     * If defined: Log message content
     */
    if (serialMqttBridge.getConfigHandler().logMqttInbound()) {
      logger.info("MQTT/IN: " + topic + " | " + new String(message.getPayload()));
    }

    String serialMessage = "";

    /*
     * If defined: Do serial message preprocessing before asking SerialHandler to send message
     */
    SerialSendPreprocessingPlugin serialSendPreprocessor = serialMqttBridge.getSerialSendPreprocessor();
    if (serialSendPreprocessor != null) {

      try {

        serialSendPreprocessor.processMqttMessage(topic, serialMqttBridge.getConfigHandler().getMqttTopicSubscribe(), message);
        serialMessage = serialSendPreprocessor.getSerialMessage();

      }
      catch (Exception e) {
        logger.error("Exception", e);
      }
    }

    /*
     * Otherwise process serial message directly
     */
    else {

      serialMessage = new String(message.getPayload());
    }

    /*
     * Ask serial handler to send message
     */
    serialMqttBridge.getSerialHandler().sendMessage(serialMessage);
  }
}
