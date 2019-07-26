class DbOperations:

    def __init__(self, conn):
        self.conn = conn

    def empty_database(self):
        cursor = self.conn.cursor()
        cursor.execute("delete from locations")
        self.conn.commit()
        cursor.close()

    def assert_there_is_location_with_name(self, name):
        found = False
        cursor = self.conn.cursor()
        cursor.execute("select lat, lon from locations where name = %s", (name, ))
        for (lat, lon) in cursor:
            found = True

        assert found
