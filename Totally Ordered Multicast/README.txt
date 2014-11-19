Total Ordered Multicast

Multicast messages exchanged between the different nodes ensure total ordering. 
The files are placed in a folder project1. project1 is the package name.

	Run the Node.java file as follows:

java package1/Node <nodeid> 
Example: java package1/Node 17

The nodes participating in the network are mentioned in the file info.text in the format:

<Node_id> <Hostname>-<Port_No> #
Example: 17 net17.utdallas.edu-6677 #


The input for the messages to be exchanged between the nodes are given in the input.txt file
in the format :

<Sender_Node>,<Receiver_Node1 Receiver_Node2 Receiver_Node3....>,<Message>
Example: 17,22 32 12,M1 