from selenium import webdriver

from db_operations import DbOperations
from locations_page import LocationsPage

import mysql.connector


def test_create_location():
    # GIVEN - Töröld az adatbázist
    conn = mysql.connector.connect(
        host="localhost",
        user="locations",
        passwd="locations",
        database="locations"
    )
    db_operations = DbOperations(conn)
    db_operations.empty_database()

    # WHEN - Vegyél fel a felületen új helyet Budapest néven, 11,5, 12,5 koordinátával
    driver = webdriver.Firefox(executable_path=r"c:\geckodriver.exe")
    page = LocationsPage(driver, "http://localhost:8080")
    page.navigate_to_page()
    page.press_create_location_link()
    page.fill_name("Budapest")
    page.fill_coords("11.5,12.5")
    page.press_create_location_button()

    # THEN - Ellenőrizd, hogy létezik Budapest a táblázatban
    page.assert_first_row("Budapest")

    # Ellenőrizd, hogy létezik Budapest az adatbázisban
    db_operations.assert_there_is_location_with_name("Budapest")

    driver.close()
