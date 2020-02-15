-- :name create-user! :<!
-- :doc creates a new user record
INSERT INTO users
(first_name, last_name, email)
VALUES (:first_name, :last_name, :email)
RETURNING (id)

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE id = :id

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE id = :id

-- :name list-users :? :*
-- :doc retrieve all users from the database
select * from users
