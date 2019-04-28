from selenium import webdriver

driver = webdriver.Firefox(executable_path="/usr/local/bin/geckodriver")
driver.get("http://localhost:8080/")

assert "Test Application" in driver.title

nameInput = driver.find_element_by_id("nameInput")
nameInput.send_keys("vicziani")
button = driver.find_element_by_xpath("/html/body/div/form/button")
button.click()

secondAnswerLabel = driver.find_element_by_xpath('/html/body/div/form/div/div[1]/label')
print(secondAnswerLabel.text)

assert "1a válasz" == secondAnswerLabel.text
assert len(secondAnswerLabel.text) == 9
assert "1a" in secondAnswerLabel.text

rb = driver.find_element_by_xpath('//*[@id="0"]')
rb.click()

labels = driver.find_elements_by_xpath("/html/body/div/form/button[2]")
for label in labels:
    print(label.text)

driver.close()