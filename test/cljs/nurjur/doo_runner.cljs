(ns nurjur.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [nurjur.core-test]))

(doo-tests 'nurjur.core-test)

