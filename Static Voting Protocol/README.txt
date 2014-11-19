Create "msgconfig.dat" file using Python Script
Run the command : "python createMsgConfig.py"
Enter Maximum number of messages to be in the input file and number of nodes running
Example: Maximum Messages = 15, Number of Nodes = 6.

Use the following command to run the program:
java Application <Total nodes> <Total Files in the System>
Example: java Application 6 6

Max Nodes entered above should be consistent. 
If Max Nodes = 10 (used to create "msgconfig.dat"), runs from Net01 to Net10.

Run the test program using command
java TestingProg <Total nodes> <Total Files in the System>
Example: java TestingProg 6 6

The ipconfig.dat file consists of the nodes net01 to net21 in the format:
<Node Id> <Port No> <IP Address> <Node Vote>	
Example: 022 31564 10.176.67.85 6
	