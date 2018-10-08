class Page:

    def __init__(self, driver):
        self.driver = driver

    def irdBeAFelhasznalonevet(self, nev):
        nameInput = self.driver.find_element_by_id("txtUserName")
        nameInput.clear()
        nameInput.send_keys(nev)

    def nyomdMegAGombot(self):
        button = self.driver.find_element_by_xpath("/html/body/form/button")
        button.click()

    def addVisszaAValaszt(self, i):
        label = self.driver.find_element_by_xpath("/html/body/form/label[{}]".format(i))
        return label.text


