
# Netty x Spring Context

The purpose of this project is to experiment and learn netty features. The project also leverages spring context 
for bean injection. There are 3 modules to this project.

- Client
- Server
- Messages


## Client
    - Starts a TCP connection
    - Sends hearbeat at rehular intreval if idleTimeout is reached

### TBD    
- Send Order Action like NewOrder, CancelOrder and ReplaceOrder
- Maintain OrderBook
- Get MarketData feed via websocket


## Server
    - Starts a Netty TCP Server
    - Accepts the connection and sends a inital ack as heartbeat
    - Sends Echo back for Heatbeat recevied

### TBD
- Accept Order Actions and ack the message
- Create a data-structure to maintain orderbook
- Create a matching engine to match orders 
- Send Market data to listeners via websocket


## Messages
    - Generates proto file for Order Action Messages

