package com.gainsight.data

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "partner_config")
class PartnerConfig (
        var name: String,
        var endpoint: String
) : AbstractJpaPersistable<Long>()