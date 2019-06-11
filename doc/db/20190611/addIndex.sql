use temail_subengine;

ALTER TABLE `contentout`
ADD INDEX `publisher_id` (`publisher_id` ASC);

ALTER TABLE `subscription`
ADD INDEX `user_id` (`user_id` ASC),
ADD INDEX `publisher_id` (`publisher_id` ASC);

ALTER TABLE `publisher`
ADD INDEX `user_id` (`user_id` ASC);


use temail_piper;

ALTER TABLE `consumer`
ADD INDEX `user_id` (`user_id` ASC);