package de.reitho.serialMqttBridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.reitho.serialMqttBridge.config.ConfigHandler;
import de.reitho.serialMqttBridge.serial.SerialHandler;

public class SerialMqttBridge {

  Logger logger = LoggerFactory.getLogger(SerialMqttBridge.class);

  private ConfigHandler configHandler;
  private SerialHandler serialHandler;

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
      logger.info("Reading configuration file...");
      configHandler = ConfigHandler.getInstance();

      // Establish serial connection
      logger.info("Building serial handler and establish connection...");
      serialHandler = SerialHandler.getInstance(configHandler);

    }
    catch (Exception e) {
      logger.error("An error occured.", e);
      System.exit(1);
    }

  }

}
