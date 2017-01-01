package de.reitho.serialMqttBridge.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.reitho.serialMqttBridge.SerialMqttBridge;
import de.reitho.serialMqttBridge.config.ConfigHandler;

public class MqttHandler {

  private Logger logger = LoggerFactory.getLogger(MqttHandler.class);

  private static MqttHandler instance;
  private SerialMqttBridge serialMqttBridge;

  private MqttClient mqttClient;

  /*********************************************************************************************************************************************************************
   * @param configHandler
   */
  private MqttHandler(SerialMqttBridge serialMqttBride) throws Exception {
    this.serialMqttBridge = serialMqttBride;
    establishSerialConnection();
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
  private void establishSerialConnection() throws Exception {
    ConfigHandler configHandler = serialMqttBridge.getConfigHandler();
    mqttClient = new MqttClient(configHandler.getMqttBrokerUrl(), configHandler.getMqttClientId(), null);
    MqttConnectOptions connOpts = new MqttConnectOptions();
    connOpts.setCleanSession(true);
    mqttClient.connect(connOpts);
  }

  /*********************************************************************************************************************************************************************
   * @param topicPath
   * @param message
   */
  public void publishMessage(String topicPath, String message) {

    MqttMessage mqttMessage = new MqttMessage(message.getBytes());
    mqttMessage.setQos(2);

    String configuredTopicPrefix = serialMqttBridge.getConfigHandler().getMqttTopicPrefix();
    String mqttPublishTopic = (configuredTopicPrefix.length() > 0 ? configuredTopicPrefix + "/" : "") + topicPath;

    /*
     * If defined: Log outgoing MQTT message
     */
    if (serialMqttBridge.getConfigHandler().logMqttOutbound()) {
      logger.info("MQTT/Out: " + mqttPublishTopic + "/" + message);
    }

    /*
     * Publish MQTT message to broker
     */

    try {
      mqttClient.publish(mqttPublishTopic, mqttMessage);
    }
    catch (Exception e) {
      logger.error("Error", e);
    }
  }

  /*********************************************************************************************************************************************************************
   *
   */
  public void processMessage() {

    if (!serialMqttBridge.isInitialized()) {
      return;
    }

  }
}
