import psycopg2
import psycopg2.extras 
import time
import sys




####################CONFIG####################
if len(sys.argv) !=  6:
	print "USAGE: python update.py host dbname user pass chunk-size"
	exit();

conf={}
conf["host"]=sys.argv[1] 
conf["dbname"]=sys.argv[2] 
conf["user"] =sys.argv[3] 
conf["pass"] =sys.argv[4] 
conf["chunk"]=int(sys.argv[5])
##############################################


####################FUNCTIONS####################
def makeSet(cols, row):
	"""This function takes the list of columns and the result set and makes the SET clause for the update query"""
	ret="SET "

	for c in cols:
		type=c['type']
		col=c['col']
		val=row[col]
		val=str(val)
		val=val.replace("'", "''")

		if val=="None":
			ret+=col + " = null ,"
		else:
			ret+=col + " = " + "'"  + str(val) + "',"
	return ret[:-1] #remove extra comma from the end of the line
#################################################


#list of columns
columns=[]


print "****************CONFIG***************"
print "Host: %(host)s\nDBName: %(dbname)s\nUser: %(user)s\nPass:*****\nChunk Size: %(chunk)d" % conf
print "*************************************"
time.sleep(20)


try:
       conn = psycopg2.connect("dbname=%(dbname)s user=%(user)s host=%(host)s password=%(pass)s" % conf)
except:
       print "I am unable to connect to the database"

print "Connected!"

cur = conn.cursor(cursor_factory=psycopg2.extras.DictCursor) # main cursor
cur2 = conn.cursor(cursor_factory=psycopg2.extras.DictCursor) # cursor for sub queries

#Get list of fields
cur.execute("SELECT data_type, column_name FROM information_schema.columns  WHERE table_name ='v_import' AND column_name != 'voyageid'")
rows = cur.fetchall()

for row in rows:
	columns.append({'col': row['column_name'], 'type' : row['data_type']})

#Update table
cur.execute("SELECT * FROM v_import ORDER BY voyageid")


rows = cur.fetchmany(conf["chunk"]) #get initial  chunk of records

while (len(rows) >0):

	for row in rows:
		voyid = row['voyageid']
 	
		Q = "UPDATE voyages "
        	Q+=makeSet(columns, row)
		Q+= " WHERE revision=1 AND suggestion='f' AND voyageid="
		Q+=str(voyid)
		cur2.execute(Q)
		print "Updated voyageid: "  + str(voyid)

	
	conn.commit()
	print "COMMIT"
	rows = cur.fetchmany(conf["chunk"]) #get more records until none are remaining
 
#clean up connections
cur2.close()
cur.close ()
conn.commit()
conn.close ()
