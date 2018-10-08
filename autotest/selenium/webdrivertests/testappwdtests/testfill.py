from selenium import webdriver

driver = webdriver.Firefox(executable_path="C:/Java/geckodriver-v0.20.1-win64/geckodriver.exe")
driver.get("http://tadev.training360.com/testapp-rest/")

assert "Test Application" in driver.title

nameInput = driver.find_element_by_id("txtUserName")
nameInput.clear()
nameInput.send_keys("vicziani")
button = driver.find_element_by_xpath("/html/body/form/button")
button.click()

secondAnswerLabel = driver.find_element_by_xpath("/html/body/form/label[2]")
print(secondAnswerLabel.text)

assert "1b válasz" == secondAnswerLabel.text
assert len(secondAnswerLabel.text) == 9
assert "1b" in secondAnswerLabel.text

rb = driver.find_element_by_xpath('//*[@id="rbnAnswer1"]')
rb.click()

labels = driver.find_elements_by_xpath("/html/body/form/label")
for label in labels:
    print(label.text)
