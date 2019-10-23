package com.example.resturant27
object CloudFirestore {
    object User {
        const val uid = "uid"
        const val update_timestamp = "update_timestamp"
        const val desc = "desc"
        const val name = "name"
        const val photo_url = "photo_url"
    }
    object Article{
        const val aid = "aid"
        const val upvote = "upvote"
        const val comments = "comments"
        const val owner_id = "owner_id"
        const val upload_timestamp = "upload_timestamp"
        const val update_timestamp = "update_timestamp"
        const val body = "body"
        object Media {
            const val objectName = "media"
            const val url = "url"
            const val type = "type"
            const val TYPE_IMAGE = "img"
        }
        object Comment{
            const val objectName = "comment"
            const val uid = "uid"
//            const val aid = "aid"
            const val comment_id = "comment_id"
            const val body = "body"
            const val upload_timestamp = "upload_timestamp"
        }
    }
    object chat{
        const val participant_1_uid = "participant_1_uid"
        const val participant_2_uid = "participant_2_uid"
        const val isP1Typing = "isP1Typing"
        const val isP2Typing = "isP2Typing"
        object Message{
            const val SENDER_P1 = 0
            const val SENDER_P2 = 1
            const val TYPE_TEXT = "txt"
            const val TYPE_IMAGE = "img"
            const val STATUS_PENDING_UPLOAD = 0
            const val STATUS_SENT = 1
            const val STATUS_RECEIVED = 2
            const val STATUS_READ = 3
            const val objectName = "message"
            const val message_id = "message_id"
            const val sender = "sender"
            const val upload_timestamp = "upload_timestamp"
            const val body = "body"
            const val type = "type"
            const val status = "status"
        }
    }
}