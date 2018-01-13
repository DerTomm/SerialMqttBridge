package de.reitho.serialMqttBridge.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.Properties;

public class ConfigHandler {

  private static ConfigHandler instance;

  private Properties prop;

  /* Serial connection properties */
  private String serialPort;
  private int baudRate;
  private int dataBits;
  private int stopBits;
  private int parity;

  /* MQTT properties */
  private String mqttBrokerUrl;
  private String mqttBrokerUsername;
  private String mqttBrokerPassword;
  private String mqttClientId;
  private String mqttTopicPublish;
  private String mqttTopicSubscribe;
  private int mqttQosSubscribe;
  private int mqttQosPublish;

  /* Logging properties */
  private boolean logSerialInbound;
  private boolean logSerialOutbound;
  private boolean logMqttInbound;
  private boolean logMqttOutbound;

  /*  Publishing preprocessor plugin properties */
  private String mqttPublishPreprocessorPlugin;
  private String serialSendPreprocessorPlugin;

  /*********************************************************************************************************************************************************************
   * @throws Exception
   */
  private ConfigHandler() throws Exception {

    prop = new Properties();
    readConfigurationFile();
  }

  /*********************************************************************************************************************************************************************
   * @return
   */
  public static ConfigHandler getInstance() throws Exception {
    if (instance == null) {
      instance = new ConfigHandler();
    }
    return instance;
  }

  /*********************************************************************************************************************************************************************
   * @throws FileNotFoundException
   */
  private void readConfigurationFile() throws Exception {

    URL urlConfigFile = new URL(ConfigHandler.class.getProtectionDomain().getCodeSource().getLocation(), "config.properties");
    File fileConfig = new File(urlConfigFile.toURI());

    try {

      prop.load(new FileReader(fileConfig));

      /* Parse serial connection properties */
      serialPort = prop.getProperty("serial.port");

      baudRate = Integer.parseInt(prop.getProperty("serial.baudRate").trim());
      dataBits = Integer.parseInt(prop.getProperty("serial.dataBits").trim());
      stopBits = Integer.parseInt(prop.getProperty("serial.stopBits").trim());
      parity = Integer.parseInt(prop.getProperty("serial.parity").trim());

      /* Parse MQTT properties */
      mqttBrokerUrl = prop.getProperty("mqtt.brokerUrl").trim();
      mqttBrokerUsername = prop.getProperty("mqtt.brokerUsername").trim();
      if ("0".equals(mqttBrokerUsername)) {
        mqttBrokerUsername = null;
      }
      mqttBrokerPassword = prop.getProperty("mqtt.brokerPassword").trim();
      if ("0".equals(mqttBrokerPassword)) {
        mqttBrokerPassword = null;
      }
      mqttClientId = prop.getProperty("mqtt.clientId").trim();
      mqttTopicPublish = prop.getProperty("mqtt.topicPublish").trim();
      mqttTopicSubscribe = prop.getProperty("mqtt.topicSubscribe").trim();
      mqttQosSubscribe = Integer.parseInt(prop.getProperty("mqtt.qosSubscribe", "0"));
      mqttQosPublish = Integer.parseInt(prop.getProperty("mqtt.qosPublish", "0"));

      /* Parse logging properties */
      logSerialInbound = Boolean.parseBoolean(prop.getProperty("logging.serialInbound").trim());
      logSerialOutbound = Boolean.parseBoolean(prop.getProperty("logging.serialOutbound").trim());
      logMqttInbound = Boolean.parseBoolean(prop.getProperty("logging.mqttInbound").trim());
      logMqttOutbound = Boolean.parseBoolean(prop.getProperty("logging.mqttOutbound").trim());

      /*  Publishing preprocessor plugin properties */
      mqttPublishPreprocessorPlugin = prop.getProperty("plugin.mqttPublishPreprocessor").trim();
      if ("0".equals(mqttPublishPreprocessorPlugin)) {
        mqttPublishPreprocessorPlugin = null;
      }
      serialSendPreprocessorPlugin = prop.getProperty("plugin.serialSendPreprocessor").trim();
      if ("0".equals(serialSendPreprocessorPlugin)) {
        serialSendPreprocessorPlugin = null;
      }

    }
    catch (Exception ex) {
      throw new Exception("Could not read config.properties file. Please refer to https://github.com/DerTomm/SerialMqttBridge for configuration documentation.", ex);
    }
  }

  /*********************************************************************************************************************************************************************
   * @return serial port name
   */
  public String getSerialPort() {
    return serialPort;
  }

  /*********************************************************************************************************************************************************************
   * @return baudrate
   */
  public int getBaudRate() {
    return baudRate;
  }

  /*********************************************************************************************************************************************************************
   * @return data bits
   */
  public int getDataBits() {
    return dataBits;
  }

  /*********************************************************************************************************************************************************************
   * @return stop bits
   */
  public int getStopBits() {
    return stopBits;
  }

  /*********************************************************************************************************************************************************************
   * @return parity
   */
  public int getParity() {
    return parity;
  }

  /*********************************************************************************************************************************************************************
   * @return flag whether incoming serial messages should be logged
   */
  public boolean logSerialInbound() {
    return logSerialInbound;
  }

  /*********************************************************************************************************************************************************************
   * @return flag whether outgoing serial messages should be logged
   */
  public boolean logSerialOutbound() {
    return logSerialOutbound;
  }

  /*********************************************************************************************************************************************************************
   * @return flag whether incoming MQTT messages should be logged
   */
  public boolean logMqttInbound() {
    return logMqttInbound;
  }

  /*********************************************************************************************************************************************************************
   * @return flag whether outgoing MQTT messages should be logged
   */
  public boolean logMqttOutbound() {
    return logMqttOutbound;
  }

  /*********************************************************************************************************************************************************************
   * @return the defined MQTT publish preprocessor plugin
   */
  public String getMqttPublishPreprocessorPlugin() {
    return mqttPublishPreprocessorPlugin;
  }

  /*********************************************************************************************************************************************************************
   * @return the defined serial sending preprocessor plugin
   */
  public String getSerialSendPreprocessorPlugin() {
    return serialSendPreprocessorPlugin;
  }

  /*********************************************************************************************************************************************************************
   * @return MQTT broker URL
   */
  public String getMqttBrokerUrl() {
    return mqttBrokerUrl;
  }

  /*********************************************************************************************************************************************************************
   * @return MQTT broker username
   */
  public String getMqttBrokerUsername() {
    return mqttBrokerUsername;
  }

  /*********************************************************************************************************************************************************************
   * @return MQTT broker password
   */
  public String getMqttBrokerPassword() {
    return mqttBrokerPassword;
  }

  /*********************************************************************************************************************************************************************
   * @return MQTT client id
   */
  public String getMqttClientId() {
    return mqttClientId;
  }

  /*********************************************************************************************************************************************************************
   * @return MQTT subscribed topic
   */
  public String getMqttTopicSubscribe() {
    return mqttTopicSubscribe;
  }

  /*********************************************************************************************************************************************************************
   * @return MQTT publishing topic
   */
  public String getMqttTopicPublish() {
    return mqttTopicPublish;
  }

  /*********************************************************************************************************************************************************************
   * @return
   */
  public int getMqttQosSubscribe() {
    return mqttQosSubscribe;
  }

  /*********************************************************************************************************************************************************************
   * @return
   */
  public int getMqttQosPublish() {
    return mqttQosPublish;
  }

}
