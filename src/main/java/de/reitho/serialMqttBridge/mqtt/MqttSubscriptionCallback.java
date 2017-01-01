package de.reitho.serialMqttBridge.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttSubscriptionCallback implements MqttCallback {

  private MqttHandler mqttHandler;

  /*********************************************************************************************************************************************************************
   * @param mqttHandler
   */
  public MqttSubscriptionCallback(MqttHandler mqttHandler) {
    this.mqttHandler = mqttHandler;
  }

  /*********************************************************************************************************************************************************************
   * @param cause
   */
  @Override
  public void connectionLost(Throwable cause) {}

  /*********************************************************************************************************************************************************************
   * @param token
   */
  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {}

  /*********************************************************************************************************************************************************************
   * @param topic
   * @param message
   * @throws Exception
   */
  @Override
  public void messageArrived(String topic, MqttMessage message) throws Exception {
    mqttHandler.processMessage(topic, message);
  }
}
