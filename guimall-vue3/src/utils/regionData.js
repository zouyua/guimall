/**
 * 基于 china-division 的省市区级联数据，适配 Ant Design Vue Cascader
 */
import provinces from 'china-division/dist/provinces.json'
import cities from 'china-division/dist/cities.json'
import areas from 'china-division/dist/areas.json'

// 建立 code → children 映射
areas.forEach(area => {
  const matchCity = cities.find(city => city.code === area.cityCode)
  if (matchCity) {
    matchCity.children = matchCity.children || []
    matchCity.children.push({ label: area.name, value: area.name })
  }
})

cities.forEach(city => {
  const matchProvince = provinces.find(province => province.code === city.provinceCode)
  if (matchProvince) {
    matchProvince.children = matchProvince.children || []
    matchProvince.children.push({
      label: city.name,
      value: city.name,
      children: city.children || []
    })
  }
})

const regionData = provinces.map(province => ({
  label: province.name,
  value: province.name,
  children: province.children || []
}))

export default regionData
