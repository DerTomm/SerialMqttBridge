# SerialMqttBridge

**Java application which forwards messages from serial ports to MQTT broker and vice-versa.**

The intention of this bridge is making data received over serial interfaces (such as UART, RS232) available to MQTT brokers for general usage - and the other way around.

I created this little application when I wanted to forward serial messages of the [MySensors](https://www.mysensors.org) home sensor/automation system to a general purpose MQTT broker to process these messages with multiple controllers (MyController and OpenHAB) for evaluation and for having a nice way of logging/debugging.

A special feature of SerialMqttBridge is a simple plugin system to modify messages before forwarding them via MQTT or UART. Sometimes it is necessary to adapt the content between both worlds. For example MySensors messages on the serial bus have to be reformatted for MQTT needs:

`12;6;1;0;0;36.5\n` (serial) <-> `Topic: <topicPrefix>/12/6/1/0/0 with content: 36.5` (MQTT)

In the source code you find a class doing this conversion in the _de.reitho.serialMqttBridge.plugins_ package. Taking this one as reference you can write your own adapters.


## Function

SerialMqttBridge gets configured with a text file called ___config.properties___. This file has to be in the application root folder.

The file contains the following parameters:
<table class="tg">
  <tr>
    <th>Config parameter</th>
    <th>Example values</th>
    <th>Note</th>
  </tr>
  <tr>
  <td colspan="3"><b>Serial connection properties</b></td>
  </tr>
  <tr>
    <td>serial.port</td>
    <td>COM5 | /dev/ttyS1</td>
    <td><br></td>
  </tr>
  <tr>
    <td>serial.baudRate<br></td>
    <td>115200</td>
    <td></td>
  </tr>
  <tr>
    <td>serial.dataBits</td>
    <td>8</td>
    <td></td>
  </tr>
  <tr>
    <td>serial.stopBits</td>
    <td>1</td>
    <td></td>
  </tr>
  <tr>
    <td>serial.parity</td>
    <td>0</td>
    <td></td>
  </tr>
  <tr>
  <td colspan="3"><b>MQTT properties</b></td>
  <tr>
    <td>mqtt.brokerUrl</td>
    <td>tcp://localhost:1883</td>
    <td>Consists of protocol (tcp://), hostname (or IP) and port.</td>
  </tr>
  <tr>
    <td>mqtt.brokerUsername</td>
    <td>jdoe</td>
    <td>optional, leave blank if no authentication needed</td>
  </tr>
  <tr>
    <td>mqtt.brokerPassword</td>
    <td>s3cr3t</td>
    <td>optional, leave blank if no authentication needed</td>
  </tr>
  <tr>
    <td>mqtt.clientId</td>
    <td>mqttSerialBridge</td>
    <td></td>
  </tr>
  <tr>
    <td>mqtt.topicPublish</td>
    <td>serialgateway-out</td>
    <td>This is the topic the bridges publishes the forwarded messages to.</td>
  </tr>
  <tr>
    <td>mqtt.topicSubscribe</td>
    <td>serialgateway-in</td>
    <td>This is the topic the bridge subscribes for messages. Wildcards '#' and '+' are supported.</td>
  </tr>
  <tr>
    <td colspan="3"><b>Logging properties</b></td>
  </tr>
  <tr>
    <td>logging.serialInbound</td>
    <td>true | false</td>
    <td>Defines whether incoming serial messages should be logged.</td>
  </tr>
  <tr>
    <td>logging.serialOutbound</td>
    <td>true | false</td>
    <td>Defines whether outgoing serial messages should be logged.</td>
  </tr>
  <tr>
    <td>logging.mqttInbound</td>
    <td>true | false</td>
    <td>Defines whether incoming MQTT messages should be logged.</td>
  </tr>
  <tr>
    <td>logging.mqttOutbound</td>
    <td>true | false</td>
    <td>Defines whether outgoing MQTT messages should be logged.</td>
  </tr>
  <tr>
    <td colspan="3"><b>Publishing preprocessor plugin properties</b></td>
  </tr>
  <tr>
    <td>plugin.mqttPublishPreprocessor</td>
    <td>com.foo.bar.MqttPreprocessor</td>
    <td>Name of the class which preprocesses the serial message before publishing it.</td>
  </tr>
  <tr>
    <td>plugin.serialSendPreprocessor</td>
    <td>com.foo.bar.SerialPreprocessor</td>
    <td>Name of the class which preprocesses the MQTT message before sending it out on serial interface.</td>
  </tr>
</table>

## Usage

After downloading and extracting the release archive please check and modify the properties file first. Then you can start the application with

`java -jar serialMqttBridge-<VERSION>.jar`.

Developers can clone the code and build the application using Maven:

`mvn package`

If you want to create your own preprocessor plugin just implement the according interfaces in the _de.reitho.serialMqttBridge.plugins_ package, put the classes into the classpath and define them in the config file.

## Outlook

Up to now you can only define one or zero preprocessor plugins. In the future I might expand this into a kind of workflow engine which allows custom logging, filtering or persistence.