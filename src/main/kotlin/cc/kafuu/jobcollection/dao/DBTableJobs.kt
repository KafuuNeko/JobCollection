package cc.kafuu.jobcollection.dao

import cc.kafuu.jobcollection.bean.JobRecord
import java.sql.ResultSet
import java.sql.Timestamp

object DBTableJobs {
    public fun getAllJob(validOnly: Boolean = true): List<JobRecord> {
        val records = ArrayList<JobRecord>()
        DBJobCollection.connect.prepareStatement(
            "SELECT * FROM jobs ${if (validOnly) "WHERE end_time>now() && start_time<now()" else ""}",
            ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY
        )?.executeQuery()?.use {
            while (it.next()) {
                records.add(JobRecord(it.getLong(1), it.getString(2), it.getTimestamp(3), it.getTimestamp(4)))
            }
        }
        return records
    }

    public fun queryJobById(jobId: Long) = DBJobCollection.connect.prepareStatement(
        "SELECT * FROM jobs WHERE job_id=?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
    )?.use { stmt ->
        stmt.setLong(1, jobId)
        stmt.executeQuery()?.use { resultSet ->
            if (resultSet.first()) {
                JobRecord(
                    resultSet.getLong(1), resultSet.getString(2), resultSet.getTimestamp(3), resultSet.getTimestamp(4)
                )
            } else {
                null
            }
        }
    }

    public fun addJob(jobName: String, startTime: Timestamp, endTime: Timestamp) =
        DBJobCollection.connect.prepareStatement("INSERT INTO jobs(job_name, start_time, end_time) VALUES (?, ?, ?)")
            ?.use { stmt ->
                stmt.setString(1, jobName)
                stmt.setTimestamp(2, startTime)
                stmt.setTimestamp(3, endTime)
                stmt.executeUpdate()
            } ?: false

    public fun updateJob(record: JobRecord) =
        DBJobCollection.connect.prepareStatement("UPDATE jobs SET job_name=?, start_time=?, end_time=? WHERE job_id=?")
            ?.use { stmt ->
                stmt.setString(1, record.jobName)
                stmt.setTimestamp(2, record.startTime)
                stmt.setTimestamp(3, record.endTime)
                stmt.setLong(4, record.jobId)
                stmt.executeUpdate()
            } ?: false

}