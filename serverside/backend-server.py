import bottle

app = bottle.Bottle()
plugin = bottle.ext.sqlite.Plugin(dbfile='../db/main.db')
app.install(plugin)

with app:

	# Returns the flight names of the current scheduled flights
	@get('/flights')
	def flights():
	return "List of flight plan names"

	# Adds a flight plan to main.db in table flight_plans
	@post('/flights')
	def add_flight():
	return "schedule flight"

def updatePlans():

return true

def getFlightNames():

return "flight names"
