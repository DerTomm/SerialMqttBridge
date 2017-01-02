package de.reitho.serialMqttBridge;

import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.reitho.serialMqttBridge.config.ConfigHandler;
import de.reitho.serialMqttBridge.mqtt.MqttHandler;
import de.reitho.serialMqttBridge.plugins.MqttPublishPreprocessingPlugin;
import de.reitho.serialMqttBridge.plugins.SerialSendPreprocessingPlugin;
import de.reitho.serialMqttBridge.serial.SerialHandler;

public class SerialMqttBridge {

  Logger logger = LoggerFactory.getLogger(SerialMqttBridge.class);

  private MqttPublishPreprocessingPlugin mqttPublishPreprocessor;
  private SerialSendPreprocessingPlugin serialSendPreprocessor;

  private ConfigHandler configHandler;
  private SerialHandler serialHandler;
  private MqttHandler mqttHandler;

  private boolean isInitialized = false;

  /*********************************************************************************************************************************************************************
   * @param args
   */
  public static void main(String[] args) {
    new SerialMqttBridge().launch();
  }

  /*********************************************************************************************************************************************************************
   *
   */
  public SerialMqttBridge() {}

  /*********************************************************************************************************************************************************************
   *
   */
  public void launch() {

    try {

      // Get config handler instance
      logger.info("Reading configuration file");
      configHandler = ConfigHandler.getInstance();

      // Instantiate preprocessor plugins if defined in config
      logger.info("Searching for plugins");
      loadPlugins();

      // Establish serial connection
      logger.info("Creating serial handler and establish connection");
      serialHandler = SerialHandler.getInstance(this);

      // Establish MQTT connection
      logger.info("Creating MQTT handler and establish connection");
      mqttHandler = MqttHandler.getInstance(this);

      isInitialized = true;
    }
    catch (Exception e) {
      logger.error("An error occured.", e);
      System.exit(1);
    }

  }

  /*********************************************************************************************************************************************************************
   *
   */
  private void loadPlugins() throws Exception {

    Reflections reflections = new Reflections();

    /*
     *  Find all MQTT publishing preprocessor plugins and load the one the user defined in configuration file - otherwise skip plugin instantiation.
     */
    String configuredMqttPreprocessorPlugin = configHandler.getMqttPublishPreprocessorPlugin();

    if (configuredMqttPreprocessorPlugin != null) {
      Set<Class<? extends MqttPublishPreprocessingPlugin>> mqttPreprocessorPlugins = reflections.getSubTypesOf(MqttPublishPreprocessingPlugin.class);
      for (Class<? extends MqttPublishPreprocessingPlugin> c : mqttPreprocessorPlugins) {
        if (c.getName().equalsIgnoreCase(configuredMqttPreprocessorPlugin)) {
          mqttPublishPreprocessor = c.newInstance();
          break;
        }
      }
      if (mqttPublishPreprocessor == null) {
        throw new Exception("Defined MQTT publish preproecessor plugin could not be found.");
      }

      logger.info("Using MQTT publish preprocessor plugin " + mqttPublishPreprocessor);
    }

    /*
     *  Find all serial send plugins and load the one the user defined in configuration file - otherwise skip plugin instantiation.
     */
    String configuredSerialPreprocessorPlugin = configHandler.getSerialSendPreprocessorPlugin();

    if (configuredSerialPreprocessorPlugin != null) {
      Set<Class<? extends SerialSendPreprocessingPlugin>> serialPreprocessorPlugins = reflections.getSubTypesOf(SerialSendPreprocessingPlugin.class);
      for (Class<? extends SerialSendPreprocessingPlugin> c : serialPreprocessorPlugins) {
        if (c.getName().equalsIgnoreCase(configuredSerialPreprocessorPlugin)) {
          serialSendPreprocessor = c.newInstance();
          break;
        }
      }
      if (serialSendPreprocessor == null) {
        throw new Exception("Defined serial send preproecessor plugin could not be found.");
      }

      logger.info("Using serial send preprocessor plugin " + serialSendPreprocessor);
    }
  }

  /*********************************************************************************************************************************************************************
   * @return
   */
  public MqttPublishPreprocessingPlugin getMqttPublishPreprocessor() {
    return mqttPublishPreprocessor;
  }

  /*********************************************************************************************************************************************************************
   * @return
   */
  public SerialSendPreprocessingPlugin getSerialSendPreprocessor() {
    return serialSendPreprocessor;
  }

  /*********************************************************************************************************************************************************************
   * @return
   */
  public ConfigHandler getConfigHandler() {
    return configHandler;
  }

  /*********************************************************************************************************************************************************************
   * @return
   */
  public SerialHandler getSerialHandler() {
    return serialHandler;
  }

  /*********************************************************************************************************************************************************************
   * @return
   */
  public MqttHandler getMqttHandler() {
    return mqttHandler;
  }

  /*********************************************************************************************************************************************************************
   * @return flag whether bridge is initialized and ready for message processing
   */
  public boolean isInitialized() {
    return isInitialized;
  }
}
