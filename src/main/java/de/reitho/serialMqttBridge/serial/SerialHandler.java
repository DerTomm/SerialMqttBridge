package de.reitho.serialMqttBridge.serial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.reitho.serialMqttBridge.SerialMqttBridge;
import de.reitho.serialMqttBridge.config.ConfigHandler;
import de.reitho.serialMqttBridge.plugins.MqttPublishPreprocessingPlugin;
import jssc.SerialPort;
import jssc.SerialPortException;

public class SerialHandler {

  private Logger logger = LoggerFactory.getLogger(SerialHandler.class);

  private static SerialHandler instance;
  private SerialMqttBridge serialMqttBridge;

  private SerialPort serialPort;

  /*********************************************************************************************************************************************************************
   * @param configHandler
   */
  private SerialHandler(SerialMqttBridge serialMqttBridge) throws Exception {
    this.serialMqttBridge = serialMqttBridge;
    establishSerialConnection();
  }

  /*********************************************************************************************************************************************************************
   * @param configHandler
   * @return
   */
  public static SerialHandler getInstance(SerialMqttBridge serialMqttBridge) throws Exception {
    if (instance == null) {
      instance = new SerialHandler(serialMqttBridge);
    }
    return instance;
  }

  /*********************************************************************************************************************************************************************
   * @throws SerialPortException
   */
  private void establishSerialConnection() throws SerialPortException {

    ConfigHandler configHandler = serialMqttBridge.getConfigHandler();
    serialPort = new SerialPort(configHandler.getSerialPort());
    serialPort.openPort();
    serialPort.setParams(configHandler.getBaudRate(), configHandler.getDataBits(), configHandler.getStopBits(), configHandler.getParity());
    serialPort.addEventListener(new SerialPortListener(this));
  }

  /*********************************************************************************************************************************************************************
   * @return serial port handle
   */
  public SerialPort getSerialPort() {
    return serialPort;
  }

  /*********************************************************************************************************************************************************************
   * @param message
   */
  public void processMessage(String message) {

    if (!serialMqttBridge.isInitialized()) {
      return;
    }

    /*
     * If defined: Log message content
     */
    if (serialMqttBridge.getConfigHandler().logSerialInbound()) {
      logger.info("Serial/IN: " + message);
    }

    String publishTopic = "";
    String publishMessage = "";

    /*
     * If defined: Do MQTT preprocessing before asking MqttHandler to publish message
     */
    MqttPublishPreprocessingPlugin mqttPublishPreprocessor = serialMqttBridge.getMqttPublishPreprocessor();
    if (mqttPublishPreprocessor != null) {

      try {

        mqttPublishPreprocessor.processSerialMessage(message);
        publishTopic = mqttPublishPreprocessor.getPublishTopic();
        publishMessage = mqttPublishPreprocessor.getPublishMessage();

      }
      catch (Exception e) {
        logger.error("Exception", e);
      }
    }

    /*
     * Otherwise process serial message directly
     */
    else {

      publishMessage = message;
    }

    /*
     * Ask MQTT handler to publish message
     */
    serialMqttBridge.getMqttHandler().publishMessage(publishTopic, publishMessage);
  }

  /*********************************************************************************************************************************************************************
   * @param serialMessage
   */
  public void sendMessage(String serialMessage) {

    /*
     * If defined: Log outgoing serial message
     */
    if (serialMqttBridge.getConfigHandler().logSerialOutbound()) {
      logger.info("Serial/Out: " + serialMessage);
    }

    /*
     * Send out serial message
     */
    try {
      serialPort.writeBytes((serialMessage + "\n").getBytes());
    }
    catch (SerialPortException e) {
      logger.error("Exception", e);
    }
  }
}
