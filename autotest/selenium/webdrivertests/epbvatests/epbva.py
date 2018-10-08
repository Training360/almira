from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By

driver = webdriver.Firefox()
driver.get("http://tadev.training360.com/epbva/")
assert "Equivalencia" in driver.title
aelem = driver.find_element_by_id("a-input")
belem = driver.find_element_by_id("b-input")
celem = driver.find_element_by_id("c-input")

driver.implicitly_wait(10)

aelem.clear()
belem.clear()
celem.clear()
aelem.send_keys("5")
belem.send_keys("10")
celem.send_keys("15")

button = driver.find_element(By.XPATH, '//button[text()="Vizsgálat"]')
button.click()

driver.implicitly_wait(5)

reselem = driver.find_element_by_class_name('alert')

print(reselem.text)


print(type(reselem.text))

assert "Nem háromszög" in reselem.text
# driver.close()