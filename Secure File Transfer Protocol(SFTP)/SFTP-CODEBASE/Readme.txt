SFTP(Secure file transfer protocol) code base:-
* In this project we copy file from one server to another using SFTP file transfer.
* SFTPProperties-workspace.properties is the file which contains the input file path and destination server directory(ramco server in this case).
* SFTPClient Folder contains the files that needs to be copied on destination server to be trigger the SFTPBatch file.
* We can use windows or any OS in-build TaskScheduler to trigger a scheduled job every day or as per the business requirement.
* Even spring batch can also be used.

Note:-
For reference we can use files inside "Secure File Transfer Protocol(SFTP)/SFTPClient/" folder for setting up the project.
