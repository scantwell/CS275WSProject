from bottle import Bottle, route, run, debug, template, request
import sqlite3
from collections import OrderedDict

dbpath="/Users/stephencantwell/Documents/JavaEclipse/repos/UAV/serverside/sqlite/main.db"

app = Bottle()

# Returns the flight names of the current scheduled flights
@app.get('/flights/')
def flights():

	conn = sqlite3.connect(dbpath)
	c = conn.cursor()
	c.execute('SELECT * FROM flightplan_names')
	conn.commit()
	res = c.fetchall()
	conn.close()

	return getFlightNames(res)

# Returns the flight plan information for a specific flight
@app.get('/flights/<name>')
def get_flight(name):
	
	conn = sqlite3.connect(dbpath)
	c = conn.cursor()
	stmt = "SELECT * FROM waypoints WHERE waypoints.FID=( SELECT flightplan_names.FID FROM flightplan_names WHERE flightplan_names.flight_name='" + str(name) + "')"
	c.execute(stmt)
	wp = c.fetchall()
	
	list=[]
	res = OrderedDict()
	res["name"] = name
	res["waypoint"]=list
	
	for i in range(0,len(wp)):
		item = OrderedDict()
		item["period"] = i
		item["name"] = wp[i][2]
		item["seq_number"] = wp[i][3]
		item["latitude"] = wp[i][4]
		item["longitude"] = wp[i][5]
		item["altitude"] = wp[i][6]
		item["deltat"] = wp[i][7]
		print "ITEM " + str(i) + ": " + str(item)
		list.append(item)

	return res 

#	return {"name": "2014_02_25_fp1","waypoint":[{"period": 0,"name": "wp1","seq_number": 1,"latitude": 50,"longitude": 60,"altitude": 100,"deltat": 4},{"period": 0,"name": "wp1","seq_number": 1,"latitude": 50,"longitude": 60,"altitude": 100,"deltat": 4}]}

# Adds a flight plan to main.db in table flight_plans
@app.post('/flights/')
def add_flight():

#	conn = sqlite3.connect(dbpath)
#	c = conn.cursor()
#	c.execute('PRAGMA FOREIGN_KEYS=1')
#	c.execute('DELETE FROM flightplan_names where fid=6')
#	conn.commit()
#	conn.close()
	return True;

def updatePlans():

	return true


# Create json for response
def getFlightNames(res):
	list=[]
	root= OrderedDict()
	for i in range(0, len(res)):
		item= OrderedDict()
		item["period"]=i
		item["name"]=res[i][1]
		list.append(item)
	root["flights"]=list	
	return root 

app.run(host='localhost', port=8080, debug=True)
