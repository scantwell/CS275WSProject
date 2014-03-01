from bottle import Bottle, route, run, debug, template, request
import sqlite3

dbpath="/Users/stephencantwell/Documents/JavaEclipse/repos/UAV/serverside/sqlite/main.db"

app = Bottle()

# Returns the flight names of the current scheduled flights
@app.get('/flights/')
def flights():
	return { "root": [{"name": "2014_02_12_fp1"},{"name": "2014_02_23_fp2"},{"name": "2014_03_14_fp3"}]}

# Returns the flight plan information for a specific flight
@app.get('/flights/<name>')
def get_flight(name):

	return {"name": "2014_02_25_fp1","waypoint":[{"period": 0,"name": "wp1","seq_number": 1,"latitude": 50,"longitude": 60,"altitude": 100,"deltat": 4},{"period": 0,"name": "wp1","seq_number": 1,"latitude": 50,"longitude": 60,"altitude": 100,"deltat": 4}]}

# Adds a flight plan to main.db in table flight_plans
@app.post('/flights/')
def add_flight():

#	conn = sqlite3.connect(dbpath)
#	c = conn.cursor()
#	c.execute('PRAGMA FOREIGN_KEYS=1')
#	c.execute('DELETE FROM flightplan_names where fid=6')
#	conn.commit()

	return True;

def updatePlans():

	return true

def getFlightNames():

	return "flight names"

app.run(host='localhost', port=8080, debug=True)
