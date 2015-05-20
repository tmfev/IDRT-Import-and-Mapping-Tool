<?xml version="1.0" encoding="UTF-8"?>

<!--<xsl:stylesheet xmlns="http://www.cdisc.org/ns/odm/v1.3" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0" xmlns:ias="http://www.secuTrial.de/cdisc/odm/v1.3">-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0" xmlns:ias="http://www.secuTrial.de/cdisc/odm/v1.3">
<!--xmlns="http://www.cdisc.org/ns/odm/v1.3" -->	
	<xsl:output omit-xml-declaration="no" indent="no"/>
	<xsl:variable name="subjectkey">nothing</xsl:variable>
	<xsl:template match="node()|@*">
		<xsl:copy>
			<!--<xsl:apply-templates xpath-default-namespace="http://www.cdisc.org/ns/odm/v1.3" select="node()|@*"/>-->
			<xsl:apply-templates select="node()|@*"/>
		</xsl:copy>
	</xsl:template>
	<!-- delete whole branches Study and AdminData -->
	<xsl:template match="/ODM/Study"/>
	<xsl:template match="/ODM/AdminData"/>
	<!-- select all SubjectData nodes -->
	
	<xsl:template match="/ODM/ClinicalData/SubjectData">
		<!-- copy the SubjectData-Node -->
		<xsl:copy>
			<!-- copy all the attributes of this SubjectData-Node -->
			<!--			<xsl:copy-of select="@*"/>-->
			<xsl:apply-templates select="StudyEventData/FormData">
				<xsl:with-param name="SubjectKey">
					<xsl:value-of select="@SubjectKey"/>
				</xsl:with-param>
				<xsl:with-param name="TransactionType">
					<xsl:value-of select="@TransactionType"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:copy>
	</xsl:template>
	<!-- save the formOID from the Form-Nodes for use in its children -->
	
	<xsl:template name="FormData" match="StudyEventData/FormData">
		<xsl:param name="SubjectKey"/>
		<xsl:param name="TransactionType"/>
		<xsl:apply-templates select="ItemGroupData/child::node()">
			<xsl:with-param name="SubjectKey">
				<xsl:value-of select="$SubjectKey"/>
			</xsl:with-param>
			<xsl:with-param name="TransactionType">
				<xsl:choose>
					<xsl:when test="not(@TransactionType)">
						<xsl:value-of select="$TransactionType"/><!--$-->
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="@TransactionType"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="FormOID">
				<xsl:value-of select="@FormOID"/>
			</xsl:with-param>
			<xsl:with-param name="StudyEventOID">
				<xsl:value-of select="../@StudyEventOID"/>
			</xsl:with-param>
			<xsl:with-param name="MetaDataVersionOID">
				<xsl:value-of select="../../../@MetaDataVersionOID"/>
			</xsl:with-param>
			<xsl:with-param name="StudyEventRepeatKey">
				<xsl:value-of select="../@StudyEventRepeatKey"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template name="ItemData" match="ItemGroupData/child::node()">
		<xsl:param name="FormOID"/>
		<xsl:param name="SubjectKey"/>
		<xsl:param name="TransactionType"/>
		<xsl:param name="StudyEventOID"/>
		<xsl:param name="MetaDataVersionOID"/>
		<xsl:param name="StudyEventRepeatKey"/>
		<xsl:copy>
			<xsl:attribute name="FormOID"><xsl:value-of select="$FormOID"></xsl:value-of></xsl:attribute>
			<xsl:attribute name="SubjectKey"><xsl:value-of select="$SubjectKey"></xsl:value-of></xsl:attribute>
			<xsl:attribute name="TransactionType">
				<xsl:choose>
					<xsl:when test="not(@TransactionType)"> <!-- ItemData-->
						<xsl:choose>
							<xsl:when test="not(../@TransactionType)"><!-- ItemGroupData-->
							<xsl:choose>
								<xsl:when test="not(../../@TransactionType)"> <!-- FormData-->
									<xsl:value-of select="$TransactionType"></xsl:value-of>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="../../@TransactionType"></xsl:value-of> <!-- FormData-->
								</xsl:otherwise>
							</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="../@TransactionType"></xsl:value-of> <!-- ItemGroupData-->
							</xsl:otherwise>
						</xsl:choose>
<!--<xsl:value-of select="$TransactionType"></xsl:value-of>-->
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="@TransactionType"></xsl:value-of> <!-- ItemData-->
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="StudyEventOID"><xsl:value-of select="$StudyEventOID"></xsl:value-of></xsl:attribute>
			<xsl:attribute name="MetaDataVersionOID"><xsl:value-of select="$MetaDataVersionOID"></xsl:value-of></xsl:attribute>
			<xsl:attribute name="StudyEventRepeatKey"><xsl:value-of select="$StudyEventRepeatKey"></xsl:value-of></xsl:attribute>
			<xsl:attribute name="ItemGroupRepeatKey"><xsl:value-of select="../@ItemGroupRepeatKey"></xsl:value-of></xsl:attribute>
			<xsl:attribute name="ItemGroupOID"><xsl:value-of select="../@ItemGroupOID"></xsl:value-of></xsl:attribute>
			<xsl:attribute name="ItemOID"><xsl:value-of select="@ItemOID"></xsl:value-of></xsl:attribute>
			<xsl:attribute name="IsNull"><xsl:value-of select="@IsNull"></xsl:value-of></xsl:attribute>
			<!--<xsl:apply-templates select="node()|@*"/>-->
	<xsl:choose>
				<xsl:when test="normalize-space(@Value) != '' ">			
						<xsl:value-of select="normalize-space(@Value)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of  select="normalize-space(.)"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
