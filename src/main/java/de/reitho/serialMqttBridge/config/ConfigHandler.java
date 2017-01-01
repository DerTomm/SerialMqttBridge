package de.reitho.serialMqttBridge.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigHandler {

  private Logger logger = LoggerFactory.getLogger(ConfigHandler.class);

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
    if (!fileConfig.exists()) {
      logger.error("Could not find config.properties file. Please refer to https://github.com/DerTomm/SerialMqttBridge for configuration documentation.");
      throw new FileNotFoundException("config.properties");
    }

    try {

      prop.load(new FileReader(fileConfig));

      /* Parse serial connection properties */
      serialPort = prop.getProperty("serialPort");

      try {

        baudRate = Integer.parseInt(prop.getProperty("baudRate"));
        dataBits = Integer.parseInt(prop.getProperty("dataBits"));
        stopBits = Integer.parseInt(prop.getProperty("stopBits"));
        parity = Integer.parseInt(prop.getProperty("parity"));
      }
      catch (NumberFormatException e) {
        throw e;
      }

      /* Parse MQTT properties */
      mqttBrokerUrl = prop.getProperty("mqttBrokerUrl");
      mqttBrokerUsername = prop.getProperty("mqttBrokerUsername");
      if (mqttBrokerUsername.equals("0")) {
        mqttBrokerUsername = null;
      }
      mqttBrokerPassword = prop.getProperty("mqttBrokerPassword");
      if (mqttBrokerPassword.equals("0")) {
        mqttBrokerPassword = null;
      }
      mqttClientId = prop.getProperty("mqttClientId");
      mqttTopicPublish = prop.getProperty("mqttTopicPublish");
      mqttTopicSubscribe = prop.getProperty("mqttTopicSubscribe");

      /* Parse logging properties */
      logSerialInbound = Boolean.parseBoolean(prop.getProperty("logSerialInbound"));
      logSerialOutbound = Boolean.parseBoolean(prop.getProperty("logSerialOutbound"));
      logMqttInbound = Boolean.parseBoolean(prop.getProperty("logMqttInbound"));
      logMqttOutbound = Boolean.parseBoolean(prop.getProperty("logMqttOutbound"));

      /*  Publishing preprocessor plugin properties */
      mqttPublishPreprocessorPlugin = prop.getProperty("mqttPublishPreprocessorPlugin");
      if (mqttPublishPreprocessorPlugin.equals("0")) {
        mqttPublishPreprocessorPlugin = null;
      }
      serialSendPreprocessorPlugin = prop.getProperty("serialSendPreprocessorPlugin");
      if (serialSendPreprocessorPlugin.equals("0")) {
        serialSendPreprocessorPlugin = null;
      }

    }
    catch (IOException ex) {
      throw ex;
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

}
