#!/usr/bin/python

from bottle import Bottle, route, run, debug, template, request, response
import sqlite3
from collections import OrderedDict

#dbpath="/Users/stephencantwell/Documents/JavaEclipse/repos/UAV/serverside/sqlite/main.db"
dbpath="C:\cygwin64\home\dmcgr_000\CS275\serverside\sqlite\main.db"

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
	res = getFlightDetails(name, wp)
	return res 

# Adds a flight plan to main.db in table flight_plans
@app.post('/flights/')
def add_flight():

	response.content_type = 'application/json'
	response.status = 200
	updateFlightPlan(request.json)

# Creates json response for a particular flight plan include waypoint array
def getFlightDetails(name, wp):
	
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

# Inserts a new flight plan into the DB
def updateFlightPlan(res):
	
	name = res["name"]
	wp = res["waypoint"]
	
	deleteFlight(name)
	insertFlightName(name)
	insertFlightPlan(name, wp)

def insertFlightPlan(name, wp):

	conn = sqlite3.connect(dbpath)
	c = conn.cursor()
	stmt = "SELECT FID FROM flightplan_names WHERE flight_name='"+name+"'"
	c.execute(stmt)
	res = c.fetchall()
	fid = res[0][0]

	for row in wp:
		stmt = "INSERT INTO waypoints (FID, waypoint_name, waypoint_number, latitude, longitude, altitude, delta_time) VALUES ("+str(fid)+",'"+row["name"]+"',"+str(row["seq_number"])+","+str(row["latitude"])+","+str(row["longitude"])+","+str(row["altitude"])+","+str(row["deltat"])+")"
		c.execute(stmt)
	conn.commit()
	c.close()

def insertFlightName(name):
	
	conn = sqlite3.connect(dbpath)
	c = conn.cursor()
	stmt = "INSERT INTO flightplan_names (flight_name) VALUES ('" + name + "')"

	c.execute(stmt)
	conn.commit()
	c.close()

def deleteFlight(name):
	
	conn = sqlite3.connect(dbpath)
	c = conn.cursor()
	stmt = "SELECT COUNT(*) FROM flightplan_names WHERE flight_name='" + name+"'"	
	num = c.execute(stmt)

	if num > 0:
		c.execute('PRAGMA FOREIGN_KEYS=1')
		c.execute('DELETE FROM flightplan_names where flight_name=\'' + name + '\'')
		conn.commit()
		conn.close()


app.run(host='localhost', port=8080, debug=True)  #localhost to 10.0.2.2  
