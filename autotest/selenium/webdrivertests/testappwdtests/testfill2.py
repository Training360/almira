from selenium import webdriver

from testappwdtests.page import Page

driver = webdriver.Firefox(executable_path="C:/Java/geckodriver-v0.20.1-win64/geckodriver.exe")
driver.get("http://tadev.training360.com/testapp-rest/")

page = Page(driver)


page.irdBeAFelhasznalonevet("vicziani")
page.nyomdMegAGombot()
valasz1 = page.addVisszaAValaszt(1)
valasz2 = page.addVisszaAValaszt(2)
valasz3 = page.addVisszaAValaszt(3)
print(valasz1)
print(valasz2)
print(valasz3)