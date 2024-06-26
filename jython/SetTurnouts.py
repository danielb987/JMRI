# Sample script showing how to multiple turnouts to 
# specific positions, with a time delay between them,
# separately from other things that the program is doing.
#
# By putting the turnout commands in a separate class, they'll
# run independently after the "start" operation
#
# Part of the JMRI distribution

# This script kicks off an independent series of actions that will take some time to 
# complete once the script itself completes.  If you are running this script as a 
# startup action, you might need to include a pause startup action before you run any
# additional scripts later.

import jmri

class setStartup(jmri.jmrit.automat.AbstractAutomaton) :      
  def init(self):
    return
  def handle(self):
    turnouts.provideTurnout("1").setState(THROWN)
    self.waitMsec(50)         # time is in milliseconds
    turnouts.provideTurnout("5").setState(CLOSED)
    self.waitMsec(50)
    turnouts.provideTurnout("19").setState(THROWN)
    return False              # all done, don't repeat again

setStartup().start()          # create one of these, and start it running
