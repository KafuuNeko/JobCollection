package cc.kafuu.jobcollection.dao

import cc.kafuu.jobcollection.bean.JobRecord
import java.sql.ResultSet

object DBTableJobs {
    public fun getAllJob(validOnly: Boolean = true): List<JobRecord> {
        val records = ArrayList<JobRecord>()
        DBJobCollection.connect.prepareStatement(
            "SELECT * FROM jobs WHERE end_time>now() && start_time<now()",
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY
        )?.executeQuery()?.use {
            while (it.next()) {
                records.add(JobRecord(it.getLong(1), it.getString(2), it.getTimestamp(3), it.getTimestamp(4)))
            }
        }
        return records
    }

    public fun queryJobById(jobId: Long) =
        DBJobCollection.connect.prepareStatement(
            "SELECT * FROM jobs WHERE job_id=?",
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY
        )?.use { stmt ->
            stmt.setLong(1, jobId)
            stmt.executeQuery()?.use {resultSet ->
                if (resultSet.first()) {
                    JobRecord(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getTimestamp(3),
                        resultSet.getTimestamp(4)
                    )
                } else {
                    null
                }
            }
        }
}