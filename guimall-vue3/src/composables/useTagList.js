import { ref, watch } from "vue"
import { useRoute, useRouter } from "vue-router"
import { useMenuStore } from "@/stores/menu"
import { setTabList, getTabList } from "@/composables/cookie"

export function useTabList() {

    const route = useRoute()
    const router = useRouter()
    const menuStore = useMenuStore()

    // 当前激活tab
    const activeTab = ref(route.path)

    // 标签列表
    const tabList = ref([
        {
            title: "仪表盘",
            path: "/admin/index"
        }
    ])

    /**
     * 初始化标签
     * 从 cookie 读取
     */
    const initTabList = () => {

        const tabs = getTabList()

        if (tabs && tabs.length > 0) {
            tabList.value = tabs
        }

    }

    initTabList()


    /**
     * 监听路由变化
     * 自动添加标签
     */
    watch(
        () => route.path,
        (newPath) => {

            activeTab.value = newPath

            const exist = tabList.value.find(item => item.path === newPath)

            if (!exist) {

                tabList.value.push({
                    title: route.meta.title || "新页面",
                    path: newPath
                })

                // 存入cookie
                setTabList(tabList.value)

            }

        },
        { immediate: true }
    )


    /**
     * 点击tab切换
     */
    const tabChange = (path) => {

        router.push(path)

    }


    /**
     * 删除tab
     */
    const removeTab = (path) => {

        if (path === "/admin/index") return

        const tabs = tabList.value
        const index = tabs.findIndex(item => item.path === path)

        tabs.splice(index, 1)

        // 如果删除的是当前页
        if (activeTab.value === path) {

            const nextTab = tabs[index] || tabs[index - 1]

            if (nextTab) {
                router.push(nextTab.path)
            }

        }

        // 更新cookie
        setTabList(tabList.value)

    }


    /**
     * 关闭其他 / 关闭全部
     */
    const handleCloseTab = (type) => {

        if (type === "closeOthers") {

            tabList.value = tabList.value.filter(
                item => item.path === activeTab.value || item.path === "/admin/index"
            )

        }

        if (type === "closeAll") {

            tabList.value = [
                {
                    title: "仪表盘",
                    path: "/admin/index"
                }
            ]

            router.push("/admin/index")

        }

        // 更新cookie
        setTabList(tabList.value)

    }


    return {

        menuStore,
        activeTab,
        tabList,
        tabChange,
        removeTab,
        handleCloseTab

    }

}