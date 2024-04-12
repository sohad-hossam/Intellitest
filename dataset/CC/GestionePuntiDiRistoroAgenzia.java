package unisa.gps.etour.control.ManagementOfRefreshmentPoints;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import unisa.gps.etour.bean.ConventionBean;
import unisa.gps.etour.bean.BeanRefreshmentpoint;
import unisa.gps.etour.bean.BeanTag;
import unisa.gps.etour.bean.BeanTourist;
import unisa.gps.etour.bean.BeanVisitPR;
/*
  * TEST CASE import
  * Unisa.gps.etour.control.ManagementOfRefreshmentPoints.test.stub.DBConvenzione;
  * Import unisa.gps.etour.control.ManagementOfRefreshmentPoints.test.stub.DBTourist;
  */
import unisa.gps.etour.repository.DBConvenzione;
import unisa.gps.etour.repository.DBTourist;
import unisa.gps.etour.repository.IDBConvenzione;
import unisa.gps.etour.util.MessageError;

/**
  * Class contentente methods for managing Refreshments by
  * Operator Agency
  *
  * @ Author Joseph Morelli
  * @ Version 0.1 © 2007 eTour Project - Copyright by DMI SE @ SA Lab --
  * University of Salerno
  */
public class ManagementOfRefreshmentPointsAgency extends "ManagementofCommonRefreshmentPoints
implements IManagementOfRefreshmentPointsAgency
(

private static final long serialVersionUID = 1L;

// Constructor
public ManagementOfRefreshmentPointsAgency () throws RemoteException
(
// Call the constructor of the inherited class to instantiate
// Database connections
super ();
dbTourist = new DBTourist ();
)

// Method that allows the operator to cancel an agency point of
// Refreshment
// Passing as parameter the ID of the same Refreshment
public boolean cancellaRefreshmentpoint (int pRefreshmentpointID)
throws RemoteException
(
// Check the validity identifier
if (pRefreshmentpointID <0)
throw new RemoteException (MessageError.Error_DATA);
TRY
(
// Execute the method that clears the Refreshment from the Database
// And in case of operation successful return true
if (RefreshmentPoint.cancellaRefreshmentpoint (pRefreshmentpointID))
return true;
)
// Exception in operations on database
catch (SQLException e)
(
System.out.println ( "Error in method cancellaRefreshmentpoint"
+ E.toString ());
throw new RemoteException (MessageError.Error_DBMS);
)
// Unexpected exceptions due to other factors
catch (Exception ee)
(
System.out.println ( "Error in method cancellaRefreshmentpoint"
Ee.toString + ());
throw new RemoteException (MessageError."Error_UNKNOWN);
)
// If no operations were successful return false end
return false;
)

// Method that allows the operator to include in the Agency database
// The new Refreshment with the information contained in the bean
public boolean insertRefreshmentpoint (BeanRefreshmentpoint pRefreshmentpoint)
throws RemoteException
(
// Check the validity of the bean as a parameter and if
// Triggers except remote
if ((pRefreshmentpoint == null)
| | (! (PRefreshmentpoint instanceof BeanRefreshmentpoint)))
throw new RemoteException (MessageError."Error_UNKNOWNError_BEAN_FORMAT);
TRY
(
// Calling the method of the class that operates on the database
// Insert the new Refreshment
if (RefreshmentPoint.insertRefreshmentpoint (pRefreshmentpoint))
// In the case where the operations were successful end
// Returns true
return true;
)
// Exception in database operations
catch (SQLException e)
(
System.out.println ( "Error in method insertRefreshmentpoint"
+ E.toString ());
throw new RemoteException (MessageError.Error_DBMS);
)
// Unexpected exceptions due to other factors
catch (Exception ee)
(
System.out.println ( "Error in method insertRefreshmentpoint"
Ee.toString + ());
throw new RemoteException (MessageError."Error_UNKNOWN);
)
// If the operation is not successful return false
return false;
)

// Method for obtaining an ArrayList with all the points Bean
// Refreshments
<BeanRefreshmentpoint> getRefreshmentPoints public ArrayList ()
throws RemoteException
(
// ArrayList to return to the end of the method
ArrayList <BeanRefreshmentpoint> toReturn = null;
TRY
(
// Get the list of Refreshments through the class
// Connect to database
// And save the list itself nell'ArrayList
toReturn = RefreshmentPoint.getListaPR ();
)
// Exception in operations on database
catch (SQLException e)
(
System.out.println ( "Error in method getRefreshmentPoints"
+ E.toString ());
throw new RemoteException (MessageError.Error_DBMS);
)
// Unexpected exceptions due to other factors
catch (Exception ee)
(
System.out.println ( "Error in method getRefreshmentPoints"
Ee.toString + ());
throw new RemoteException (MessageError."Error_UNKNOWN);
)
// Check the ArrayList to return so as not to pass null values
// To the caller
if (null == toReturn)
throw new RemoteException (MessageError."Error_UNKNOWNError_BEAN_FORMAT);
// Return the ArrayList with all the refreshment
toReturn return;
)

// Method that allows you to get all the refreshment that have
// A Convention on or off depending on the parameter passed
public ArrayList <BeanRefreshmentpoint> getRefreshmentPoints (
conventionStatus boolean) throws RemoteException
(
// Array that allows me to store all the refreshment and
// Which will remove
// Depending on the parameter passed to the refreshment active or not
ArrayList <BeanRefreshmentpoint> toReturn = null;
// Array that allows me to store all the refreshment active
// Using the database connection
ArrayList <BeanRefreshmentpoint> active = null;
// Instance to connect to the database
IDBConvenzione conv = new DBConvenzione ();
TRY
(
// Connect all proceeds from the refreshment Assets
conv.getListActiveConventionPR assets = ();
)
// Exception in operations on database
catch (SQLException e)
(
System.out
. System.out.println ( "Error in method getRefreshmentPoints (boolean)"
+ E.toString ());
throw new RemoteException (MessageError.Error_DBMS);
)
// Unexpected exceptions due to other factors
catch (Exception ee)
(
System.out
. System.out.println ( "Error in method getRefreshmentPoints (boolean)"
Ee.toString + ());
throw new RemoteException (MessageError."Error_UNKNOWN);
)
// If you want to get the refreshment active, then return
// Directly to those passed by the connection to the database
if (conventionStatus)
(
// Check the contents dell'ArrayList so as not to return
// Null values to the caller
if (active == null)
throw new RemoteException (MessageError."Error_UNKNOWNError_BEAN_FORMAT);
return active;
)
else
(
TRY
(
// Connect all proceeds from the refreshment then
// Perform comparisons
toReturn = RefreshmentPoint.getListPR ();
)
// Exception in operations on database
catch (SQLException e)
(
System.out
. System.out.println ( "Error in method getRefreshmentPoints (boolean)"
+ E.toString ());
throw new RemoteException (MessageError.Error_DBMS);
)
// Unexpected exceptions due to other factors
catch (Exception ee)
(
System.out
. System.out.println ( "Error in method getRefreshmentPoints (boolean)"
Ee.toString + ());
throw new RemoteException (MessageError."Error_UNKNOWN);
)
// Size dell'ArrayList containing all of gourmet
// Could
// Change size if you remove some element
int dim = toReturn.size ();
// Variable that allows me to understand whether to remove a Point
// Refreshments
// From array that then I must return
boolean present = false;
// First loop to loop through all the ArrayList elements of
// All Refreshments
for (int i = 0; i <dim; i + +)
(
// Second loop to loop through all the ArrayList elements
// Cones just Refreshments active
for (int j = 0 j <attivi.size () j + +)
(
// If the catering points in question has the ID equal to one
// Of those assets, then set this to true
if (attivi.get (j). getId () == toReturn.get (i). getId ())
present = true;
)
// If the catering points in question has a Convention active
// Removes it from those to be returned
if (present)
toReturn.remove (i);
)
)
// Return the ArrayList obtained
toReturn return;
)

// Method that allows you to change the past as a refreshment
// Parameter
public boolean modificationRefreshmentpoint (
BeanRefreshmentpoint pRefreshmentpointUpdated)
throws RemoteException
(
// Check the validity of the bean as a parameter and if
// Trigger an exception remote
if (null == pRefreshmentpointUpdated
| | (! (PRefreshmentpointUpdated instanceof BeanRefreshmentpoint)))
throw new RemoteException (MessageError."Error_UNKNOWNError_BEAN_FORMAT);
TRY
(
// Call the method to change the database connection
// The Refreshment
if (RefreshmentPoint.modificationRefreshmentpoint (pRefreshmentpointUpdated))
// Return a positive value if the operation was successful
// End
return true;
)
// Exception in operations on database
catch (SQLException e)
(
System.out.println ( "Error in method modificationRefreshmentpoint"
+ E.toString ());
throw new RemoteException (MessageError.Error_DBMS);
)
// Unexpected exceptions due to other factors
catch (Exception ee)
(
System.out.println ( "Error in method modificationRefreshmentpoint"
Ee.toString + ());
throw new RemoteException (MessageError."Error_UNKNOWN);
)
// Return false if the operation is successful you should
return false;
)

// Method to obtain the Bean a particular point
// Refreshment whose
// Identifier is passed as parameter
public BeanRefreshmentpoint getRefreshmentpoint (int pRefreshmentpointID)
throws RemoteException
(
// Check the validity identifier
if (pRefreshmentpointID <0)
throw new RemoteException (MessageError.Error_DATA);
// Bean to return to the caller
BeanRefreshmentpoint toReturn = null;
TRY
(
// Revenue catering points in the issue by connecting to
// Database
toReturn = RefreshmentPoint.getRefreshmentpoint (pRefreshmentpointID);
)
// Exception in the database opearazioni
catch (SQLException e)
(
throw new RemoteException (MessageError.Error_DBMS);
)
// Unexpected exceptions caused by other factors
catch (Exception ee)
(
System.out.println ( "Error in method getRefreshmentpoint"
Ee.toString + ());
throw new RemoteException (MessageError."Error_UNKNOWN);
)
// Check the bean to be returned in order not to return null values
// To the caller
if (null == toReturn)
throw new RemoteException (MessageError."Error_UNKNOWNError_BEAN_FORMAT);
// Return the bean of Refreshment
toReturn return;
)

// Method that allows you to activate a particular convention to a Point
// Passed as parameter Refreshments
public boolean attivaConvenzione (int pRefreshmentpointID,
ConventionBean pConv) throws RemoteException
(
// Check the validity of parameters passed
if ((pRefreshmentpointID <0) | | (pConv == null)
| | (! (PConv instanceof ConventionBean)))
throw new RemoteException (MessageError.Error_DATA);
// Check the data further
if (pConv.getIdRefreshmentpoint ()! = pRefreshmentpointID)
throw new RemoteException (MessageError.Error_DATA);
IDBConvenzione conv = null;
TRY
(
// Instantiate the class to connect to the database
conv = new DBConvenzione ();
// If the Convention is not yet active, previously provided to
// Activate it locally and then pass the bean to the database changed
if (conv.getConvezioneAttiva (pRefreshmentpointID) == null)
(
pConv.setAttiva (true);
conv.modificationConvenzione (pConv);
return true;
)
)
// Exception in operations on database
catch (SQLException e)
(
throw new RemoteException (MessageError.Error_DBMS);
)
// Unexpected exceptions caused by other factors
catch (Exception ee)
(
System.out.println ( "Error in method attivaConvenzione"
Ee.toString + ());
throw new RemoteException (MessageError."Error_UNKNOWN);
)
// If the operation is successful you should return false
return false;
)

// Method that returns a HashMap containing, for Refreshment
// Passed as a parameter, the feedback associated with it
public HashMap <BeanVisitPR, String> getFeedbackRefreshmentpoint (
pRefreshmentpointID int) throws RemoteException
(
// Check the ID passed as a parameter
if (pRefreshmentpointID <0)
throw new RemoteException (MessageError.Error_DATA);
// Instantiate the map and the performance of ArrayList that I will use
// Method
HashMap <BeanVisitPR, String> mapReturn = null;
ArrayList <BeanVisitPR> bVisit = null;
TRY
(
// Here I take the list of all visits to the PR passed as
// Parameter
bVisit = feed.getListaVisitPR (pRefreshmentpointID);
// Instantiate the map of the same size as the list of
// BeanVisitPR
mapReturn = new HashMap <BeanVisitPR, String> (bVisit.size ());
// Here we begin to iterate on each visit to add its
// Username
for (Iterator <BeanVisitPR> iteratoreVisitPR = bVisit.iterator (); iteratoreVisitPR
. hasNext ();)
(
// Recuperto the BeanVisitPR
BeanVisitPR bVisitTemp = iteratoreVisitPR.next ();
// Retrieve the tourist who left the comment that I
// Examining
BeanTourist bTouristTemp = dbTourist.getTourist (bVisitTemp
. getIdTourist ());
// Get the username of the Tourist
String usernameTouristTemp = bTouristTemp.getUsername ();
// Put the pair in the map
mapReturn.put (bVisitTemp, usernameTouristTemp);
)
)
// Exception in database operations
catch (SQLException e)
(
System.out.println ( "Error in method getFeedbackPR"
+ E.toString ());
throw new RemoteException (MessageError.Error_DBMS);
)
// Unexpected exceptions caused by other factors
catch (Exception ee)
(
System.out.println ( "Error in method getFeedbackPR"
Ee.toString + ());
throw new RemoteException (MessageError."Error_UNKNOWN);
)
// Check the return parameter so as not to pass null values
// To the database
if (null == mapReturn)
throw new RemoteException (MessageError."Error_UNKNOWNError_BEAN_FORMAT);
mapReturn return;
)

// Method to insert a tag from those of a refreshment
public boolean cancellaTagRefreshmentpoint (pRefreshmentpointId int, int pTagId)
throws RemoteException
(
// Check the validity of past data
if ((pRefreshmentpointId <0) | | (pTagId <0))
throw new RemoteException (MessageError.Error_DATA);
// ArrayList which stores all the tags
ArrayList <BeanTag> tags;
// Boolean variable to check if the Refreshment
// Holds the tag you want to delete
boolean present = false;
TRY
(
// Use the method through the class of database connection
tags = tag.getTagRefreshmentpoint (pRefreshmentpointId);
)
// Exception in the execution of transactions in database
catch (SQLException e)
(
System.out.println ( "Error in method cancellaTagRefreshmentpoint"
+ E.toString ());
throw new RemoteException (MessageError.Error_DBMS);
)
// Unexpected exception due to other factors
catch (Exception ee)
(
System.out.println ( "Error in method cancellaTagRefreshmentpoint"
Ee.toString + ());
throw new RemoteException (MessageError."Error_UNKNOWN);
)
// Check if the tag is present cycle currently
// Between those of Refreshment
for (t BeanTag: tags)
if (t.getId () == pTagId)
present = true;
// If the tag is present among those of eateries, then
// Provides for executing the erase operation
if (present)
(
TRY
(
return tag.cancellaTagRefreshmentpoint (pRefreshmentpointId, pTagId);
)
// Exception in implementing the operation on the database
catch (SQLException e)
(
System.out
. System.out.println ( "Error in method cancellaTagRefreshmentpoint"
+ E.toString ());
throw new RemoteException (MessageError.Error_DBMS);
)
// Unexpected exception due to other factors
catch (Exception ee)
(
System.out
. System.out.println ( "Error in method cancellaTagRefreshmentpoint"
Ee.toString + ());
throw new RemoteException (MessageError."Error_UNKNOWN);
)
)
// In case something did not come to fruition
// Return false
return false;
)

// Method to delete a tag from those of a refreshment
// The operations are identical to those above, except for
// Control over the presence of the tag from those of Refreshment
// Which should give negative results, and the call here is the method of
// Insert
public boolean insertTagRefreshmentpoint (pRefreshmentpointId int, int pTagId)
throws RemoteException
(
if ((pRefreshmentpointId <0) | | (pTagId <0))
throw new RemoteException (MessageError.Error_DATA);

ArrayList <BeanTag> tags;
boolean present = false;
TRY
(
tags = tag.getTagRefreshmentpoint (pRefreshmentpointId);
)
catch (SQLException e)
(
System.out.println ( "Error in method insertTagRefreshmentpoint"
+ E.toString ());
throw new RemoteException (MessageError.Error_DBMS);
)
catch (Exception ee)
(
System.out.println ( "Error in method insertTagRefreshmentpoint"
Ee.toString + ());
throw new RemoteException (MessageError."Error_UNKNOWN);
)
for (t BeanTag: tags)
if (t.getId () == pTagId)
present = true;
// Check that the Refreshment has not already specified tag
if (present)
(
TRY
(
// Calling the method of adding the class via
// Connect to database
return tag.aggiungeTagRefreshmentpoint (pRefreshmentpointId, pTagId);
)
catch (SQLException e)
(
System.out
. System.out.println ( "Error in method insertTagRefreshmentpoint"
+ E.toString ());
throw new RemoteException (MessageError.Error_DBMS);
)
catch (Exception ee)
(
System.out
. System.out.println ( "Error in method insertTagRefreshmentpoint"
Ee.toString + ());
throw new RemoteException (MessageError."Error_UNKNOWN);
)
)
// Return false if some operation is not successful you should
return false;
)
)
