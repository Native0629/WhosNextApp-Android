package com.app.whosnextapp.utility;

public class ConstantEnum {
    public enum SnippetType {
        IMAGE(1),
        VIDEO(2),
        AUDIO(3);

        private int Id;

        SnippetType(final int id) {
            this.Id = id;
        }

        public int getId() {
            return Id;
        }
    }

    public enum GroupInviteStatus {
        UPLOADED("1"),
        REJECT("2"),
        PENDING("3");

        private String Id;

        GroupInviteStatus(final String id) {
            this.Id = id;
        }

        public String getId() {
            return Id;
        }
    }

    public enum PostType {
        NORMAL("1"),
        BCL("3");

        private String Id;

        PostType(final String id) {
            this.Id = id;
        }

        public String getId() {
            return Id;
        }
    }

    public enum NotificationType {
        group_notification(1),
        one_to_one_notification(2),
        message_center_notification(3),
        group_invitation(4),
        new_blog(5),
        mention_user(6),
        program_comment(7),
        content_release(8),
        survey_notification(9),
        calendar_event(10),
        challenge(11),
        challenge_comments(12), // N/A for android
        challenge_mention_user(13), // N/A for android
        check_in_member_added(14),
        check_in_survey_open_team_member(15),
        check_in_survey_open_leader(16),
        check_in_survey_feedback_reminder_24_hrs(17),
        check_in_survey_feedback_reminder_46_hrs(18),
        check_in_3_days_before_survey(19),
        check_in_2_respondents_answer(20),
        check_in_after_24_hrs_less_2_answer(21),
        check_in_after_48_hrs_when_survey_ends(22);

        private int Id;

        NotificationType(final int id) {
            this.Id = id;
        }

        public int getId() {
            return Id;
        }
    }
}
