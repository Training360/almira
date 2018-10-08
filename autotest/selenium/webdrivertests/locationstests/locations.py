from selenium import webdriver

driver = webdriver.Firefox(executable_path="C:/Java/geckodriver-v0.20.1-win64/geckodriver.exe")
driver.get("http://tadev.training360.com/locations-app/")

with open("../data.csv") as f:
    for line in f:
        fields = line.split(",")
        print(fields)

        nameInput = driver.find_element_by_id("location-name")
        nameInput.clear()
        nameInput.send_keys(fields[0])

        nameInput = driver.find_element_by_id("location-coords")
        nameInput.clear()
        nameInput.send_keys(fields[1] + "," + fields[2].strip())

        button = driver.find_element_by_xpath("/html/body/div/form[1]/input[3]")
        button.click()

