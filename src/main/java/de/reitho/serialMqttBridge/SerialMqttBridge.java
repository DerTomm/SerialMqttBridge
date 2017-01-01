package de.reitho.serialMqttBridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.reitho.serialMqttBridge.config.ConfigHandler;
import jssc.SerialPort;
import jssc.SerialPortException;

public class SerialMqttBridge {

  Logger logger = LoggerFactory.getLogger(SerialMqttBridge.class);

  private ConfigHandler configHandler;

  private SerialPort serialPort;

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

      // Establish serial connection
      logger.info("Establishing serial connection");
      establishSerialConnection();

    }
    catch (Exception e) {
      logger.error("An error occured.", e);
      System.exit(1);
    }

  }

  /*********************************************************************************************************************************************************************
   * @throws SerialPortException
   */
  private void establishSerialConnection() throws SerialPortException {

    serialPort = new SerialPort(configHandler.getSerialPort());
    serialPort.openPort();
    serialPort.setParams(configHandler.getBaudRate(), configHandler.getDataBits(), configHandler.getStopBits(), configHandler.getParity());
  }
}
