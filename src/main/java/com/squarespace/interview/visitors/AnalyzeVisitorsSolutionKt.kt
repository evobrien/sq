package com.squarespace.interview.visitors

object AnalyzeVisitorsSolutionKt {


    data class Visit constructor(var sessionCount:Int,var lastVisitTimeStamp:Long)

    /**
     * Given an [Iterable] of website hit data modeled as [WebsiteVisit]
     * analyze it and return the resulting page views, unique visitors, and
     * sessions.
     *
     * @param websiteVisits Input hit data.
     * @return A 3-element array of long corresponding to
     * 0: page views, 1: unique visitors, 2: view sessions
     */
    @JvmStatic
    fun processPageViews(websiteVisits: Iterable<WebsiteVisit>): LongArray {

        val uniques= mutableMapOf<String,Visit>()
        var pageViews=0
        for(websiteVisit in websiteVisits){
            val item=uniques[websiteVisit.visitorId]
            if (item == null){
                uniques[websiteVisit.visitorId] = Visit(1,lastVisitTimeStamp = websiteVisit.timestamp)
            }else{
                //if the diff between the current timestamp and the last timestamp is >=30
                // then there is a new session else we're part of the current session
               val sessionDiffMins=(websiteVisit.timestamp - item.lastVisitTimeStamp)/60
               if(sessionDiffMins >30){
                   item.sessionCount++
               }
                item.lastVisitTimeStamp=websiteVisit.timestamp
            }
            pageViews++
        }

        var sessions=0
        for(unique in uniques){
            sessions+=unique.value.sessionCount
        }

        return arrayOf(pageViews.toLong(), uniques.size.toLong(), sessions.toLong()).toLongArray()
    }

}
