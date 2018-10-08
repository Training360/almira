from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from selenium.common.exceptions import TimeoutException

driver = webdriver.Firefox(executable_path="C:/Java/geckodriver-v0.20.1-win64/geckodriver.exe")
driver.get("http://localhost:8080")

element_present = EC.presence_of_element_located((By.XPATH, '/html/body/div/table/tr[1]'))
WebDriverWait(driver, 5).until(element_present)

deletebuttons = driver.find_elements_by_xpath("/html/body/div/table/tr/td[4]/button[2]")

for i in range(0, len(deletebuttons)):
    button = driver.find_element_by_xpath("/html/body/div/table/tr[1]/td[4]/button[2]")
    button.click()
    element_present = EC.presence_of_element_located((By.XPATH, '/html/body/div/table/tr[1]'))
    WebDriverWait(driver, 5).until(element_present)
