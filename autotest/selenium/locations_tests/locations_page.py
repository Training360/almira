from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as ec
from selenium.webdriver.common.by import By
from selenium.common.exceptions import TimeoutException


class LocationsPage:

    def __init__(self, driver, environment):
        self.driver = driver
        self.environment = environment

    def navigate_to_page(self):
        self.driver.get(self.environment)

    def press_create_location_link(self):
        # Aki beleolvasott, és eljutott idáig, kap egy rozsomákpacsit
        create_location_link = self.driver.find_element_by_id("create-location-link")
        create_location_link.click()

    def fill_name(self, name):
        name_input = self.driver.find_element_by_id("location-name")
        name_input.clear()
        name_input.send_keys(name)

    def fill_coords(self, coords):
        name_input = self.driver.find_element_by_id("location-coords")
        name_input.clear()
        name_input.send_keys(coords)

    def press_create_location_button(self):
        create_location_button = self.driver.find_element_by_xpath("/html/body/div/form[1]/input[1]")
        create_location_button.click()

    def assert_first_row(self, name):
        element_present = ec.text_to_be_present_in_element((By.XPATH, '/html/body/div/table/tr[1]/td[2]'), name)
        try:
            WebDriverWait(self.driver, 5).until(element_present)
        except TimeoutException:
            assert False, "There is no row in the table, or the value of second cell is not" + name
