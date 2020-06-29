printjson("************Executing script********************");
var mongo = new Mongo("uat.amtrust.asia:27017");
var db =mongo.getDB("admin");
db.auth("root" , "dbP@ssw0rd");
var policyDb = mongo.getDB("policyDB");
var collection = policyDb.getCollection('policyDocuments').find({ $and: [ { partnerId: { $eq: 'VNSAMSUOEMNA02' } } , { $or: [ {balanceRRP :{$exists: false}  }, {balanceRRP :{$eq: 0}  }, {balanceRRP :{$eq: ''}  } ] } ] });
var countOfRecordsProcessed = 0;
var numberofRecordsStreamed = 1;
// printjson("Matched records======"+collection.length());
collection.forEach((policydoc) =>{
    printjson("Streaming record number "+numberofRecordsStreamed);
	try{
    if((policydoc.balanceRRP == null || policydoc.balanceRRP == '' || policydoc.balanceRRP == '0') && (policydoc.partnerId == 'VNSAMSUOEMNA02')){ 
        var creationDateString = policydoc.creationDate;
        var parts = creationDateString.split("/");
        var creationDate =  new Date(parts[2], parts[1] - 1, parts[0]);
        
        var referenceDate = new Date(2020 ,5,12);

        printjson(creationDate +"------------"+ referenceDate + "---------"+policydoc.deviceRRP +"--------------"+ policydoc.balanceRRP);
        printjson(referenceDate);
        printjson(creationDate < referenceDate);
        if(creationDate < referenceDate){
        	policydoc.balanceRRP = policydoc.deviceRRP;
        	policyDb.getCollection('policyDocuments').save(policydoc);
            printjson("Policy doc modified with policy number"+policydoc.policyNumber);
        	countOfRecordsProcessed++;
        }
    }
	}catch(err){
		printjson("========Exception occured========="+err+"======for policy number====="+policydoc.policyNumber);
	}
    numberofRecordsStreamed++;
});
printjson("************Records processed********************"+countOfRecordsProcessed);
printjson("************Finshed script********************");