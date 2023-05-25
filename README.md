# ChitChatServer

Het there!

ChitChat App is a TCP/IP based Client-Server application that allows multiple clients to chat with each other. It provides a user-friendly GUI developed in Java Swing.
This repository is the server side of the application, if you want, this is the repository to the client side - https://github.com/KarinOchayon070/ChitChatClient.git.

## Features

- Real-time chat between multiple clients connected to the server.
- Ability to send private direct messages to specific participants.
- GUI design implemented using the Composite design pattern.
- Event handling mechanism built with the Observer design pattern.
- Client GUI has two states: Connected and Disconnected, with distinct visual appearance.
- Command pattern used for sending messages to all participants or privately.
- Server-side implements the Iterator design pattern using the java.util.Iterator interface.

## Design Patterns

- Proxy: The Socket objects on both the server and client sides are surrogated using the Proxy design pattern.
- Composite: The client's GUI is developed using Swing and implements the Composite design pattern.
- Observer: GUI event handling is implemented using the Observer design pattern, leveraging Java's built-in events handling mechanism.
- State: The client's GUI has two states: Connected and Disconnected, altering the visual appearance of buttons.
- Command: Sending messages to specific participants or all participants is implemented using the Command pattern.
- Iterator: The server-side indirectly implements the Iterator design pattern using the java.util.Iterator interface.
