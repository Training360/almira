from selenium import webdriver

from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from selenium.common.exceptions import TimeoutException

driver = webdriver.Firefox(executable_path="C:/Java/geckodriver-v0.20.1-win64/geckodriver.exe")
driver.get("http://localhost:8080")

element_present = EC.presence_of_element_located((By.XPATH, '/html/body/div/table/tr[1]'))
print(element_present)
try:
    WebDriverWait(driver, 5).until(element_present)
except TimeoutException:
    print("Nincs")

# link = driver.find_element_by_xpath('//*[@id="create-location-link"]')
# print(link.text)

editbuttons = driver.find_elements_by_xpath("/html/body/div/table/tr/td[4]/button[1]")

for editbutton in editbuttons:
    print(editbutton.text)