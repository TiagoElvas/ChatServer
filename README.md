# ChatServer

**ChatServer** is a Java-based, terminal-driven chat application that supports multiple clients connecting to a server for real-time communication. Built with a client-server model using TCP connections, ChatServer allows users to join a chat room, exchange messages, and enjoy interactive commands, such as random joke generation, to enhance the chat experience.

## Features

- **Multiplayer Chat Room**: Multiple clients can connect to the server and chat in real-time.
- **Interactive Commands**:
  - `/name [your_name]`: Set a unique username to join the chat.
  - `/joke`: Get a random joke for some added fun.
  - `/Exit`: Leave the chat room gracefully.
- **Multithreading**: Ensures smooth and responsive chat by handling multiple clients simultaneously on separate threads.


## Technologies

- **Java Sockets**: Manages TCP connections between the server and clients.
- **Multithreading**: Uses `ExecutorService` to handle multiple clients concurrently, allowing each client to send and receive messages seamlessly.
- **I/O Streams**: Implements `BufferedReader` and `PrintWriter` for efficient communication.
- **Command Parsing**: Supports user commands like `/name`, `/joke`, and `/Exit` for chatroom functionality and interaction.
